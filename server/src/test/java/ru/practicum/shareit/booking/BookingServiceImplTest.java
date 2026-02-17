package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ForbiddenException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock BookingRepository bookings;

    @InjectMocks BookingServiceImpl service;

    @Test
    void approve_whenNotOwner_shouldThrowForbidden() {
        Booking booking = Booking.builder()
                .id(1L)
                .item(ru.practicum.shareit.item.model.Item.builder()
                        .owner(ru.practicum.shareit.user.User.builder().id(99L).build())
                        .build())
                .status(BookingStatus.WAITING)
                .build();

        when(bookings.findFullById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> service.approve(1L, 1L, true))
                .isInstanceOf(ForbiddenException.class);
    }
}
