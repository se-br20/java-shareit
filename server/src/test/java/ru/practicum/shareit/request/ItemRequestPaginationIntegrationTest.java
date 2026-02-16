package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemRequestPaginationIntegrationTest {

    @Autowired MockMvc mvc;

    @Test
    void getAll_negativeFrom_shouldReturn400() throws Exception {

        mvc.perform(get("/users")
                .contentType("application/json"));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_sizeZero_shouldReturn400() throws Exception {
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }
}