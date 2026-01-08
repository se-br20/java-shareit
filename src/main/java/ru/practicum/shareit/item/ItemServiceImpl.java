package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository items;
    private final UserRepository users;

    @Override
    public ItemDto create(Long ownerId, ItemDto dto) {
        requireUser(ownerId);

        Item toSave = Item.builder()
                .name(dto.getName().trim())
                .description(dto.getDescription().trim())
                .available(dto.getAvailable())
                .ownerId(ownerId)
                .build();

        return ItemMapper.toDto(items.save(toSave));
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto dto) {
        requireUser(ownerId);

        Item existing = items.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        if (!ownerId.equals(existing.getOwnerId())) {
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

        Item updated = Item.builder()
                .id(existing.getId())
                .name(name != null ? name.trim() : null)
                .description(description != null ? description.trim() : null)
                .available(available)
                .ownerId(existing.getOwnerId())
                .build();

        return ItemMapper.toDto(items.update(updated));
    }

    @Override
    public ItemDto getById(Long requesterId, Long itemId) {
        requireUser(requesterId);
        Item item = items.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
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
        return items.searchAvailable(text.trim()).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    private void requireUser(Long userId) {
        users.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

}