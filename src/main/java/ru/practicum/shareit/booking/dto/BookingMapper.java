package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {

    public static BookingDto toDto(Booking b) {
        if (b == null) return null;

        BookingDto dto = new BookingDto();
        dto.setId(b.getId());
        dto.setStart(b.getStart());
        dto.setEnd(b.getEnd());
        dto.setStatus(b.getStatus());

        dto.setBooker(new BookerDto(b.getBooker().getId()));
        dto.setItem(new ItemShortDto(b.getItem().getId(), b.getItem().getName()));
        return dto;
    }
}
