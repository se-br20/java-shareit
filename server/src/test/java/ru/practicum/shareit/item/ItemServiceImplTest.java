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
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository items;

    @Mock
    private UserRepository users;

    @Mock
    private BookingRepository bookings;

    @Mock
    private CommentRepository comments;

    @InjectMocks
    private ItemServiceImpl service;

    @Test
    void addComment_withoutPastApprovedBooking_shouldThrowValidation() {
        long userId = 1L;
        long itemId = 10L;

        User user = new User();
        user.setId(userId);
        user.setName("u");
        user.setEmail("u@u.ru");

        Item item = new Item();
        item.setId(itemId);
        item.setName("item");
        item.setDescription("d");
        item.setAvailable(true);

        when(users.findById(userId)).thenReturn(Optional.of(user));
        when(items.findById(itemId)).thenReturn(Optional.of(item));

        when(bookings.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
                eq(itemId),
                eq(userId),
                eq(BookingStatus.APPROVED),
                any(LocalDateTime.class)
        )).thenReturn(false);

        CommentCreateDto dto = new CommentCreateDto();
        dto.setText("hi");

        assertThatThrownBy(() -> service.addComment(userId, itemId, dto))
                .isInstanceOf(ValidationException.class);
    }
}