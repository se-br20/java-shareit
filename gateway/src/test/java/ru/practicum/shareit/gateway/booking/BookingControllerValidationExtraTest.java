package ru.practicum.shareit.gateway.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerValidationExtraTest {

    @Autowired MockMvc mvc;
    @MockitoBean BookingClient client;

    @Test
    void approve_negativeBookingId_shouldReturn400() throws Exception {
        mvc.perform(patch("/bookings/-1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }
}