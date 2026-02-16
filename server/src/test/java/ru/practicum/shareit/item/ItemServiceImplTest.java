package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock ItemRepository items;
    @Mock UserRepository users;
    @Mock BookingRepository bookings;
    @Mock CommentRepository comments;
    @Mock ru.practicum.shareit.request.ItemRequestRepository requests;

    @InjectMocks ItemServiceImpl service;

    @Test
    void addComment_withoutPastApprovedBooking_shouldThrowValidation() {
        when(users.findById(1L)).thenReturn(Optional.of(ru.practicum.shareit.user.User.builder().id(1L).name("u").build()));
        when(items.findById(10L)).thenReturn(Optional.of(Item.builder().id(10L).build()));

        when(bookings.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
                10L, 1L, BookingStatus.APPROVED, LocalDateTime.now()
        )).thenReturn(false);

        CommentCreateDto dto = new CommentCreateDto();
        dto.setText("ok");

        assertThatThrownBy(() -> service.addComment(1L, 10L, dto))
                .isInstanceOf(ValidationException.class);
    }
}