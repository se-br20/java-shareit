package ru.practicum.shareit.gateway.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateDto;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER) @Positive Long userId,
                                         @Valid @RequestBody BookingCreateDto dto) {
        return client.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(USER_HEADER) @Positive Long userId,
                                          @PathVariable @Positive Long bookingId,
                                          @RequestParam boolean approved) {
        return client.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_HEADER) @Positive Long userId,
                                          @PathVariable @Positive Long bookingId) {
        return client.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getByBooker(@RequestHeader(USER_HEADER) @Positive Long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return client.getByBooker(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwner(@RequestHeader(USER_HEADER) @Positive Long userId,
                                             @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return client.getByOwner(userId, state);
    }
}
