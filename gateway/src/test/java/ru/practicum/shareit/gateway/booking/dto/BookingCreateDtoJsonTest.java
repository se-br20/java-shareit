package ru.practicum.shareit.gateway.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingCreateDtoJsonTest {

    @Autowired
    JacksonTester<BookingCreateDto> json;

    @Test
    void serialize_shouldContainFields() throws Exception {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.parse("2026-01-01T10:00:00"));
        dto.setEnd(LocalDateTime.parse("2026-01-01T12:00:00"));

        var content = json.write(dto);

        assertThat(content).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.start").isEqualTo("2026-01-01T10:00:00");
        assertThat(content).extractingJsonPathStringValue("$.end").isEqualTo("2026-01-01T12:00:00");
    }
}
