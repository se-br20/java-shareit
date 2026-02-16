package ru.practicum.shareit.gateway.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    JacksonTester<ItemDto> json;

    @Test
    void deserialize_shouldReadFields() throws Exception {
        String body = "{"
                + "\"id\":10,"
                + "\"requestId\":5,"
                + "\"name\":\"Drill\","
                + "\"description\":\"Good drill\","
                + "\"available\":true"
                + "}";

        ItemDto dto = json.parse(body).getObject();

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getRequestId()).isEqualTo(5L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getAvailable()).isTrue();
    }
}