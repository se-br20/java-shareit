package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(USER_HEADER) Long userId,
                                 @RequestBody ItemRequestCreateDto dto) {
        return service.create(userId, dto);
    }

    @GetMapping
    public List<ItemRequestDto> getOwn(@RequestHeader(USER_HEADER) Long userId) {
        return service.getOwn(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOthers(@RequestHeader(USER_HEADER) Long userId,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        return service.getOthers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(USER_HEADER) Long userId,
                                  @PathVariable Long requestId) {
        return service.getById(userId, requestId);
    }
}
