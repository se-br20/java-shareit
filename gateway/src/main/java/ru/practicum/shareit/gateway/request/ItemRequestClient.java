package ru.practicum.shareit.gateway.request;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.config.GatewayProperties;

import java.util.Map;

@Component
public class ItemRequestClient extends BaseClient {
    public ItemRequestClient(GatewayProperties props) {
        super(props.getUrl());
    }

    public ResponseEntity<Object> create(Long userId, Object body) {
        return post("/requests", userId, body);
    }

    public ResponseEntity<Object> getOwn(Long userId) {
        return get("/requests", userId);
    }

    public ResponseEntity<Object> getAll(Long userId, int from, int size) {
        return get("/requests/all", userId, Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> getById(Long userId, Long requestId) {
        return get("/requests/" + requestId, userId);
    }
}
