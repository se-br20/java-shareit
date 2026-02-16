package ru.practicum.shareit.gateway.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerSearchTest {

    @Autowired MockMvc mvc;
    @MockitoBean ItemClient client;

    @Test
    void search_blankText_shouldReturn200AndEmptyArray() throws Exception {
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "   "))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
