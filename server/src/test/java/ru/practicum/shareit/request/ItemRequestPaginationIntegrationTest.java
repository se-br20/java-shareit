package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemRequestPaginationIntegrationTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() throws Exception {
        String email = "u-" + UUID.randomUUID() + "@u.ru";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"u\",\"email\":\"" + email + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_negativeFrom_shouldReturn400() throws Exception {
        mvc.perform(get("/requests/all")
                        .header(USER_HEADER, "1")
                        .param("from", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_sizeZero_shouldReturn400() throws Exception {
        mvc.perform(get("/requests/all")
                        .header(USER_HEADER, "1")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }
}