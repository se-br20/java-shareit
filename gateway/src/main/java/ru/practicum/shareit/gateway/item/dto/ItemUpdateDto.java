package ru.practicum.shareit.gateway.item.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemUpdateDto {

    @Positive
    private Long requestId;

    @Size(min = 1, message = "Item name must not be blank")
    private String name;

    @Size(min = 1, message = "Item description must not be blank")
    private String description;

    private Boolean available;
}