package ru.practicum.shareit.gateway.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.item.dto.CommentCreateDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.item.dto.ItemUpdateDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER) @Positive Long userId,
                                         @Valid @RequestBody ItemDto dto) {
        return client.create(userId, dto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(USER_HEADER) @Positive Long userId,
                                         @PathVariable @Positive Long itemId,
                                         @Valid @RequestBody ItemUpdateDto dto) {
        return client.update(userId, itemId, dto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_HEADER) @Positive Long userId,
                                          @PathVariable @Positive Long itemId) {
        return client.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@RequestHeader(USER_HEADER) @Positive Long userId) {
        return client.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(USER_HEADER) @Positive Long userId,
                                         @RequestParam(name = "text", required = false) String text) {
        if (text == null || text.isBlank()) {

            return ResponseEntity.ok().body(java.util.List.of());
        }
        return client.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_HEADER) @Positive Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @Valid @RequestBody CommentCreateDto dto) {
        return client.addComment(userId, itemId, dto);
    }
}
