package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public final class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toDto(Item item) {
        if (item == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static ItemResponseDto toResponseDto(Item item) {
        if (item == null) return null;
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }
}