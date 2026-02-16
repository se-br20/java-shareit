package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exception.ForbiddenException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookings;
    private final UserRepository users;
    private final ItemRepository items;

    private static final Sort SORT_NEW_TO_OLD = Sort.by(Sort.Direction.DESC, "start");

    @Override
    public BookingDto create(Long userId, BookingCreateDto dto) {
        User booker = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Item item = items.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found: " + dto.getItemId()));

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Item not found: " + item.getId());
        }

        if (!Boolean.TRUE.equals(item.getAvailable())) {
            throw new ValidationException("Item is not available for booking");
        }

        if (dto.getStart() == null || dto.getEnd() == null || !dto.getEnd().isAfter(dto.getStart())) {
            throw new ValidationException("Invalid booking time range");
        }

        Booking saved = bookings.save(Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build());

        return BookingMapper.toDto(saved);
    }

    @Override
    public BookingDto approve(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = bookings.findFullById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + bookingId));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("Access denied");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Booking status cannot be changed");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toDto(bookings.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getById(Long userId, Long bookingId) {
        users.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Booking b = bookings.findFullById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + bookingId));

        boolean isBooker = b.getBooker().getId().equals(userId);
        boolean isOwner = b.getItem().getOwner().getId().equals(userId);

        if (!isBooker && !isOwner) {
            throw new NotFoundException("Booking not found: " + bookingId);
        }

        return BookingMapper.toDto(b);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getByBooker(Long userId, BookingState state) {
        users.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));
        LocalDateTime now = LocalDateTime.now();

        BookingState effective = (state == null) ? BookingState.ALL : state;

        List<Booking> list = switch (effective) {
            case ALL -> bookings.findByBooker_Id(userId, SORT_NEW_TO_OLD);
            case CURRENT -> bookings.findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId, now, now, SORT_NEW_TO_OLD);
            case PAST -> bookings.findByBooker_IdAndEndIsBefore(userId, now, SORT_NEW_TO_OLD);
            case FUTURE -> bookings.findByBooker_IdAndStartIsAfter(userId, now, SORT_NEW_TO_OLD);
            case WAITING -> bookings.findByBooker_IdAndStatus(userId, BookingStatus.WAITING, SORT_NEW_TO_OLD);
            case REJECTED -> bookings.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED, SORT_NEW_TO_OLD);
        };

        return list.stream().map(BookingMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getByOwner(Long ownerId, BookingState state) {
        users.findById(ownerId).orElseThrow(() -> new NotFoundException("User not found: " + ownerId));
        LocalDateTime now = LocalDateTime.now();

        BookingState effective = (state == null) ? BookingState.ALL : state;

        List<Booking> list = switch (effective) {
            case ALL -> bookings.findByItem_Owner_Id(ownerId, SORT_NEW_TO_OLD);
            case CURRENT ->
                    bookings.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, SORT_NEW_TO_OLD);
            case PAST -> bookings.findByItem_Owner_IdAndEndIsBefore(ownerId, now, SORT_NEW_TO_OLD);
            case FUTURE -> bookings.findByItem_Owner_IdAndStartIsAfter(ownerId, now, SORT_NEW_TO_OLD);
            case WAITING -> bookings.findByItem_Owner_IdAndStatus(ownerId, BookingStatus.WAITING, SORT_NEW_TO_OLD);
            case REJECTED -> bookings.findByItem_Owner_IdAndStatus(ownerId, BookingStatus.REJECTED, SORT_NEW_TO_OLD);
        };

        return list.stream().map(BookingMapper::toDto).toList();
    }
}