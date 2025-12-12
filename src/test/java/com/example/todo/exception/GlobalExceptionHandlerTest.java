package com.example.todo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link GlobalExceptionHandler}.  Ensures that runtime
 * exceptions are translated into a 404 NOT_FOUND response with a body
 * containing the expected error details.
 */
class GlobalExceptionHandlerTest {

    @Test
    void handleRuntimeShouldReturnNotFound() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        RuntimeException ex = new RuntimeException("Item missing");
        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Item missing", body.get("title"));
        assertEquals(404, body.get("status"));
        // ensure a timestamp is present and parseable
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp").toString().contains("T"));
    }
}