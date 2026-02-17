package ru.practicum.shareit.gateway.item;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.config.GatewayProperties;

import java.util.Map;

@Component
public class ItemClient extends BaseClient {

    public ItemClient(GatewayProperties props) {
        super(props.getUrl());
    }

    public ResponseEntity<Object> create(Long userId, Object body) {
        return post("/items", userId, body);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, Object body) {
        return patch("/items/" + itemId, userId, body);
    }

    public ResponseEntity<Object> getById(Long userId, Long itemId) {
        return get("/items/" + itemId, userId);
    }

    public ResponseEntity<Object> getOwnerItems(Long userId) {
        return get("/items", userId);
    }

    public ResponseEntity<Object> search(Long userId, String text) {
        return get("/items/search", userId, Map.of("text", text));
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, Object body) {
        return post("/items/" + itemId + "/comment", userId, body);
    }
}
