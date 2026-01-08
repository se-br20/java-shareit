package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public UserDto create(UserCreateDto dto) {
        String email = dto.getEmail().trim();
        if (repo.existsByEmail(email, null)) {
            throw new ConflictException("Email already exists");
        }
        User saved = repo.save(UserMapper.fromCreateDto(dto));
        return UserMapper.toDto(saved);
    }

    @Override
    public UserDto update(Long userId, UserUpdateDto dto) {
        User existing = repo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        String name = dto.getName() != null ? dto.getName() : existing.getName();
        String email = dto.getEmail() != null ? dto.getEmail() : existing.getEmail();

        if (email != null && email.isBlank()) {
            throw new ValidationException("Email must not be blank");
        }
        if (name != null && name.isBlank()) {
            throw new ValidationException("Name must not be blank");
        }
        if (dto.getEmail() != null) {
            String newEmail = dto.getEmail().trim();
            if (repo.existsByEmail(newEmail, userId)) {
                throw new ConflictException("Email already exists");
            }
            email = newEmail;
        }

        User updated = User.builder()
                .id(userId)
                .name(name)
                .email(email)
                .build();

        return UserMapper.toDto(repo.update(updated));
    }

    @Override
    public UserDto getById(Long userId) {
        return repo.findById(userId)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

    @Override
    public List<UserDto> getAll() {
        return repo.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Override
    public void delete(Long userId) {
        repo.deleteById(userId);
    }

}