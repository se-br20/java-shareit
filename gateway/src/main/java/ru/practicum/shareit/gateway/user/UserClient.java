package ru.practicum.shareit.gateway.user;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.config.GatewayProperties;

@Component
public class UserClient extends BaseClient {

    public UserClient(GatewayProperties props) {
        super(props.getUrl());
    }

    public ResponseEntity<Object> create(Object body) {

        return rest.post().uri("/users").body(body).retrieve().toEntity(Object.class);
    }

    public ResponseEntity<Object> update(Long userId, Object body) {
        return rest.patch().uri("/users/{id}", userId).body(body).retrieve().toEntity(Object.class);
    }

    public ResponseEntity<Object> getById(Long userId) {
        return rest.get().uri("/users/{id}", userId).retrieve().toEntity(Object.class);
    }

    public ResponseEntity<Object> getAll() {
        return rest.get().uri("/users").retrieve().toEntity(Object.class);
    }

    public ResponseEntity<Object> delete(Long userId) {
        return rest.delete().uri("/users/{id}", userId).retrieve().toEntity(Object.class);
    }
}
