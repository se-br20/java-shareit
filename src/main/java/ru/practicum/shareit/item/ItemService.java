package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto create(Long ownerId, ItemDto dto);

    ItemDto update(Long ownerId, Long itemId, ItemDto dto);

    ItemResponseDto getById(Long requesterId, Long itemId);

    List<ItemResponseDto> getOwnerItems(Long ownerId);

    List<ItemDto> search(Long requesterId, String text);

    CommentDto addComment(Long userId, Long itemId, CommentCreateDto dto);
}