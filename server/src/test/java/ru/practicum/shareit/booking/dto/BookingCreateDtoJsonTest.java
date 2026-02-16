package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingCreateDtoJsonTest {

    @Autowired JacksonTester<BookingCreateDto> json;

    @Test
    void serialize_containsFields() throws Exception {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.of(2026, 1, 1, 10, 0));
        dto.setEnd(LocalDateTime.of(2026, 1, 1, 12, 0));

        var content = json.write(dto);

        assertThat(content).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(content).hasJsonPathStringValue("$.start");
        assertThat(content).hasJsonPathStringValue("$.end");
    }
}
