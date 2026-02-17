package ru.practicum.shareit.gateway.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.config.GatewayProperties;

import java.util.Map;

@Component
public class BookingClient extends BaseClient {

    public BookingClient(GatewayProperties props) {
        super(props.getUrl());
    }

    public ResponseEntity<Object> create(Long userId, Object body) {
        return post("/bookings", userId, body);
    }

    public ResponseEntity<Object> approve(Long userId, Long bookingId, boolean approved) {
        return patch("/bookings/" + bookingId, userId, Map.of("approved", approved));
    }

    public ResponseEntity<Object> getById(Long userId, Long bookingId) {
        return get("/bookings/" + bookingId, userId);
    }

    public ResponseEntity<Object> getByBooker(Long userId, String state) {
        return get("/bookings", userId, Map.of("state", state));
    }

    public ResponseEntity<Object> getByOwner(Long userId, String state) {
        return get("/bookings/owner", userId, Map.of("state", state));
    }
}
