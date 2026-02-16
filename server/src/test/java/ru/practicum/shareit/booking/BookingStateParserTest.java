package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ValidationException;

import static org.assertj.core.api.Assertions.*;

class BookingStateParserTest {

    @Test
    void parse_null_shouldReturnAll() {
        assertThat(BookingStateParser.parse(null)).isEqualTo(BookingState.ALL);
    }

    @Test
    void parse_unknown_shouldThrowValidation() {
        assertThatThrownBy(() -> BookingStateParser.parse("WRONG"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Unknown state");
    }
}
