package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void approve_byWrongUser_shouldReturn403() throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"owner\",\"email\":\"o@o.ru\"}"))
                .andExpect(status().isOk());

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"booker\",\"email\":\"b@b.ru\"}"))
                .andExpect(status().isOk());

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"item\",\"description\":\"d\",\"available\":true}"))
                .andExpect(status().isOk());

        String start = LocalDateTime.now().plusDays(1).toString();
        String end = LocalDateTime.now().plusDays(2).toString();
        String bookingBody = String.format("{\"itemId\":1,\"start\":\"%s\",\"end\":\"%s\"}", start, end);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingBody))
                .andExpect(status().isOk());

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", "2")
                        .param("approved", "true"))
                .andExpect(status().isForbidden());
    }
}