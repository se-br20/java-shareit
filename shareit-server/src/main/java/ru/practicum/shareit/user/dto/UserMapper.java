package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static User fromDto(UserDto dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static User fromCreateDto(UserCreateDto dto) {
        if (dto == null) return null;
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
