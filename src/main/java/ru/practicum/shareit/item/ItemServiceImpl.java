package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
    private static final PageRequest ONE_RECORD = PageRequest.of(0, 1);

    private final ItemRepository items;
    private final UserRepository users;
    private final BookingRepository bookings;
    private final CommentRepository comments;

    @Override
    public ItemDto create(Long ownerId, ItemDto dto) {
        User owner = requireUser(ownerId);

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ValidationException("Item name must not be blank");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new ValidationException("Item description must not be blank");
        }
        if (dto.getAvailable() == null) {
            throw new ValidationException("Item available is required");
        }

        Item saved = items.save(Item.builder()
                .name(dto.getName().trim())
                .description(dto.getDescription().trim())
                .available(dto.getAvailable())
                .owner(owner)
                .build());

        return ItemMapper.toDto(saved);
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto dto) {
        requireUser(ownerId);

        Item existing = items.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        if (!existing.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Item not found: " + itemId);
        }

        String name = (dto != null && dto.getName() != null) ? dto.getName() : existing.getName();
        String description = (dto != null && dto.getDescription() != null) ? dto.getDescription() : existing.getDescription();
        Boolean available = (dto != null && dto.getAvailable() != null) ? dto.getAvailable() : existing.getAvailable();

        if (name != null && name.isBlank()) {
            throw new ValidationException("Item name must not be blank");
        }
        if (description != null && description.isBlank()) {
            throw new ValidationException("Item description must not be blank");
        }

        existing.setName(name != null ? name.trim() : null);
        existing.setDescription(description != null ? description.trim() : null);
        existing.setAvailable(available);

        return ItemMapper.toDto(items.save(existing));
    }

    @Override
    public ItemResponseDto getById(Long requesterId, Long itemId) {
        requireUser(requesterId);

        Item item = items.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        ItemResponseDto out = ItemMapper.toResponseDto(item);
        out.setComments(CommentMapper.toDtoList(comments.findByItem_Id(itemId)));

        if (item.getOwner().getId().equals(requesterId)) {
            fillLastNext(out, itemId);
        }

        return out;
    }

    @Override
    public List<ItemResponseDto> getOwnerItems(Long ownerId) {
        requireUser(ownerId);

        List<Item> ownerItems = items.findByOwner_Id(ownerId, SORT_BY_ID_ASC);
        if (ownerItems.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = ownerItems.stream().map(Item::getId).toList();

        Map<Long, List<Comment>> commentsByItem = comments.findByItem_IdIn(itemIds).stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId()));

        // Bulk загрузка всех APPROVED по всем itemIds (N+1 устранён)
        List<Booking> approvedBookings = bookings.findApprovedForItems(itemIds, BookingStatus.APPROVED);

        Map<Long, List<Booking>> bookingsByItem = approvedBookings.stream()
                .collect(Collectors.groupingBy(b -> b.getItem().getId()));

        LocalDateTime now = LocalDateTime.now();

        return ownerItems.stream().map(item -> {
            ItemResponseDto out = ItemMapper.toResponseDto(item);

            out.setComments(CommentMapper.toDtoList(commentsByItem.getOrDefault(item.getId(), List.of())));

            List<Booking> itemBookings = bookingsByItem.getOrDefault(item.getId(), List.of());

            Booking last = itemBookings.stream()
                    .filter(b -> !b.getStart().isAfter(now))
                    .max(Comparator.comparing(Booking::getStart))
                    .orElse(null);

            Booking next = itemBookings.stream()
                    .filter(b -> b.getStart().isAfter(now))
                    .min(Comparator.comparing(Booking::getStart))
                    .orElse(null);

            if (last != null) {
                out.setLastBooking(new BookingShortDto(last.getId(), last.getBooker().getId()));
            }
            if (next != null) {
                out.setNextBooking(new BookingShortDto(next.getId(), next.getBooker().getId()));
            }

            return out;
        }).toList();
    }

    @Override
    public List<ItemDto> search(Long requesterId, String text) {
        requireUser(requesterId);

        if (text == null || text.isBlank()) {
            return List.of();
        }

        return items.searchAvailable(text.trim()).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentCreateDto dto) {
        User author = requireUser(userId);

        Item item = items.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        if (dto.getText() == null || dto.getText().isBlank()) {
            throw new ValidationException("Comment text must not be blank");
        }

        boolean hasPastApprovedBooking = bookings.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now()
        );

        if (!hasPastApprovedBooking) {
            throw new ValidationException("User has not completed a booking for this item");
        }

        Comment saved = comments.save(Comment.builder()
                .text(dto.getText().trim())
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build());

        return CommentMapper.toDto(saved);
    }

    private User requireUser(Long userId) {
        return users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

    private void fillLastNext(ItemResponseDto out, Long itemId) {
        LocalDateTime now = LocalDateTime.now();

        bookings.findLastApprovedForItem(itemId, BookingStatus.APPROVED, now, ONE_RECORD)
                .stream()
                .findFirst()
                .ifPresent(last -> out.setLastBooking(new BookingShortDto(last.getId(), last.getBooker().getId())));

        bookings.findNextApprovedForItem(itemId, BookingStatus.APPROVED, now, ONE_RECORD)
                .stream()
                .findFirst()
                .ifPresent(next -> out.setNextBooking(new BookingShortDto(next.getId(), next.getBooker().getId())));
    }


}