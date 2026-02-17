package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock ItemRequestRepository requests;
    @Mock UserRepository users;
    @Mock ru.practicum.shareit.item.ItemRepository items;

    @InjectMocks ItemRequestServiceImpl service;

    @Test
    void create_blankDescription_shouldThrowValidation() {
        when(users.findById(1L)).thenReturn(Optional.of(ru.practicum.shareit.user.User.builder().id(1L).build()));

        ItemRequestCreateDto dto = new ItemRequestCreateDto();
        dto.setDescription("   ");

        assertThatThrownBy(() -> service.create(1L, dto))
                .isInstanceOf(ValidationException.class);
    }
}
