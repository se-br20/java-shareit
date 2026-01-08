package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "Item name is required")
    private String name;

    @NotBlank(message = "Item description is required")
    private String description;

    @NotNull(message = "Item available is required")
    private Boolean available;
}