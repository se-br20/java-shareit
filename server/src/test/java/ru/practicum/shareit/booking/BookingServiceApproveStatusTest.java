package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceApproveStatusTest {

    @Mock BookingRepository bookings;

    @InjectMocks BookingServiceImpl service;

    @Test
    void approve_whenStatusNotWaiting_shouldThrowValidation() {
        Booking booking = Booking.builder()
                .id(1L)
                .item(ru.practicum.shareit.item.model.Item.builder()
                        .owner(ru.practicum.shareit.user.User.builder().id(1L).build())
                        .build())
                .status(BookingStatus.APPROVED)
                .build();

        when(bookings.findFullById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> service.approve(1L, 1L, true))
                .isInstanceOf(ValidationException.class);
    }
}
