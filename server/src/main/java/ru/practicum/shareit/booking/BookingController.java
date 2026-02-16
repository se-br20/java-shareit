package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final BookingService service;

    @PostMapping
    public BookingDto create(@RequestHeader(USER_HEADER) Long userId,
                             @Valid @RequestBody BookingCreateDto dto) {
        return service.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(USER_HEADER) Long userId,
                              @PathVariable Long bookingId,
                              @RequestParam boolean approved) {
        return service.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(USER_HEADER) Long userId,
                              @PathVariable Long bookingId) {
        return service.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getByBooker(@RequestHeader(USER_HEADER) Long userId,
                                        @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return service.getByBooker(userId, BookingStateParser.parse(state));
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwner(@RequestHeader(USER_HEADER) Long userId,
                                       @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return service.getByOwner(userId, BookingStateParser.parse(state));
    }
}
