package ru.practicum.shareit.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
public class GatewayErrorHandler {

     @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleBodyValidation(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Validation failed"));
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, String>> handleParamValidation(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Validation failed"));
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<String> handleServerError(RestClientResponseException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(e.getResponseBodyAsString());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, String>> handleOther(Throwable e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Unexpected error"));
    }
}
