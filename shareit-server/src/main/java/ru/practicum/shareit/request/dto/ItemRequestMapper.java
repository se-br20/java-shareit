package ru.practicum.shareit.request.dto;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest r, List<ItemForRequestDto> items) {
        if (r == null) return null;

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(r.getId());
        dto.setDescription(r.getDescription());
        dto.setCreated(r.getCreated());
        dto.setItems(items);
        return dto;
    }
}
