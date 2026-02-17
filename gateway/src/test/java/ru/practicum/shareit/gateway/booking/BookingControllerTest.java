package ru.practicum.shareit.gateway.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired MockMvc mvc;
    @MockitoBean BookingClient client;

    @Test
    void getByBooker_negativeUserId_badRequest() throws Exception {
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByOwner_ok() throws Exception {
        when(client.getByOwner(anyLong(), anyString()))
                .thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}