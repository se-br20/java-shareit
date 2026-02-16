package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader(USER_HEADER) Long userId,
                          @Valid @RequestBody ItemDto dto) {
        return service.create(userId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody(required = false) ItemDto dto) {
        return service.update(userId, itemId, dto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@RequestHeader(USER_HEADER) Long userId,
                                   @PathVariable Long itemId) {
        return service.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemResponseDto> getOwnerItems(@RequestHeader(USER_HEADER) Long userId) {
        return service.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(USER_HEADER) Long userId,
                                @RequestParam(name = "text", required = false) String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return service.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentCreateDto dto) {
        return service.addComment(userId, itemId, dto);
    }
}