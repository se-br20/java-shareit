package ru.practicum.shareit.gateway.client;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class BaseClient {
    protected static final String USER_HEADER = "X-Sharer-User-Id";
    protected final RestClient rest;

    public BaseClient(String serverUrl) {
        this.rest = RestClient.builder().baseUrl(serverUrl).build();
    }

    protected ResponseEntity<Object> get(String path, Long userId) {
        return rest.get()
                .uri(path)
                .header(USER_HEADER, String.valueOf(userId))
                .retrieve()
                .toEntity(Object.class);
    }

    protected ResponseEntity<Object> get(String path, Long userId, Map<String, ?> params) {
        return rest.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path(path);
                    params.forEach(b::queryParam);
                    return b.build();
                })
                .header(USER_HEADER, String.valueOf(userId))
                .retrieve()
                .toEntity(Object.class);
    }

    protected ResponseEntity<Object> post(String path, Long userId, Object body) {
        return rest.post()
                .uri(path)
                .header(USER_HEADER, String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(Object.class);
    }

    protected ResponseEntity<Object> patch(String path, Long userId, Object body) {
        return rest.patch()
                .uri(path)
                .header(USER_HEADER, String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(Object.class);
    }

    protected ResponseEntity<Object> patch(String path, Long userId, Map<String, ?> params) {
        return rest.patch()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path(path);
                    params.forEach(b::queryParam);
                    return b.build();
                })
                .header(USER_HEADER, String.valueOf(userId))
                .retrieve()
                .toEntity(Object.class);
    }

    protected ResponseEntity<Object> delete(String path, Long userId) {
        return rest.delete()
                .uri(path)
                .header(USER_HEADER, String.valueOf(userId))
                .retrieve()
                .toEntity(Object.class);
    }
}