package ru.practicum.shareit.gateway.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.user.dto.UserCreateDto;
import ru.practicum.shareit.gateway.user.dto.UserUpdateDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserCreateDto dto) {
        return client.create(dto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable @Positive Long userId,
                                         @Valid @RequestBody UserUpdateDto dto) {
        return client.update(userId, dto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable @Positive Long userId) {
        return client.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return client.getAll();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long userId) {
        return client.delete(userId);
    }
}
