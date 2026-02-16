package ru.practicum.shareit.booking;


import ru.practicum.shareit.exception.ValidationException;

public final class BookingStateParser {
    private BookingStateParser() {
    }

    public static BookingState parse(String value) {
        if (value == null) {
            return BookingState.ALL;
        }
        try {
            return BookingState.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Unknown state: " + value);
        }
    }
}
