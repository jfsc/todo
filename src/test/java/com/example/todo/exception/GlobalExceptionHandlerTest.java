package com.example.todo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void handleNotFoundReturns404WithProblemDetailsBody() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        String message = "resource not found";

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(new NoSuchElementException(message));

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("about:blank", body.get("type"));
        assertEquals(message, body.get("title"));
        assertEquals(HttpStatus.NOT_FOUND.value(), ((Number) body.get("status")).intValue());

        String timestamp = (String) body.get("timestamp");
        assertNotNull(timestamp);
        assertDoesNotThrow(() -> Instant.parse(timestamp));
    }

    @Test
    void handleBadRequestReturns400WithPassedMessage() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        String message = "invalid argument";

        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(new IllegalArgumentException(message));

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("about:blank", body.get("type"));
        assertEquals(message, body.get("title"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), ((Number) body.get("status")).intValue());
        assertDoesNotThrow(() -> Instant.parse((String) body.get("timestamp")));
    }

    @Test
    void handleRuntimeReturns500WithGenericMessage() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(new RuntimeException("boom"));

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("about:blank", body.get("type"));
        assertEquals("Erro interno do servidor", body.get("title"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((Number) body.get("status")).intValue());
        assertDoesNotThrow(() -> Instant.parse((String) body.get("timestamp")));
    }
}

