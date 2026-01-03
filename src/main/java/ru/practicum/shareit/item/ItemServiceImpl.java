package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository items;
    private final UserRepository users;

    @Override
    public ItemDto create(Long ownerId, ItemCreateDto dto) {
        requireUser(ownerId);
        validateCreate(dto);

        Item saved = items.save(ItemMapper.fromCreateDto(dto, ownerId));
        return ItemMapper.toDto(saved);
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemUpdateDto dto) {
        requireUser(ownerId);

        Item existing = items.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
        if (!ownerId.equals(existing.getOwnerId())) {
            throw new NotFoundException("Item not found: " + itemId); // классика ShareIt: чужое как "не найдено"
        }

        String name = dto.getName() != null ? dto.getName() : existing.getName();
        String description = dto.getDescription() != null ? dto.getDescription() : existing.getDescription();
        Boolean available = dto.getAvailable() != null ? dto.getAvailable() : existing.getAvailable();

        if (name != null && name.isBlank()) throw new ValidationException("Item name must not be blank");
        if (description != null && description.isBlank()) throw new ValidationException("Item description must not be blank");

        Item updated = Item.builder()
                .id(existing.getId())
                .name(name)
                .description(description)
                .available(available)
                .ownerId(existing.getOwnerId())
                .build();

        return ItemMapper.toDto(items.update(updated));
    }

    @Override
    public ItemDto getById(Long requesterId, Long itemId) {
        requireUser(requesterId);
        Item item = items.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getOwnerItems(Long ownerId) {
        requireUser(ownerId);
        return items.findByOwnerId(ownerId).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public List<ItemDto> search(Long requesterId, String text) {
        requireUser(requesterId);
        return items.searchAvailable(text).stream().map(ItemMapper::toDto).toList();
    }

    private void requireUser(Long userId) {
        users.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

    private void validateCreate(ItemCreateDto dto) {
        if (dto == null) throw new ValidationException("Item body is required");
        if (dto.getName() == null || dto.getName().isBlank()) throw new ValidationException("Item name is required");
        if (dto.getDescription() == null || dto.getDescription().isBlank()) throw new ValidationException("Item description is required");
        if (dto.getAvailable() == null) throw new ValidationException("Item available is required");
    }
}
