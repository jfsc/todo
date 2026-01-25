package com.example.todo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntimeShouldReturnNotFoundStatus() {
        RuntimeException ex = new RuntimeException("Test exception");
        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleRuntimeShouldReturnCorrectBodyStructure() {
        String errorMessage = "Resource not found";
        RuntimeException ex = new RuntimeException(errorMessage);
        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertNotNull(response.getBody());
        Map<String, Object> body = response.getBody();

        assertTrue(body.containsKey("type"));
        assertEquals("about:blank", body.get("type"));

        assertTrue(body.containsKey("title"));
        assertEquals(errorMessage, body.get("title"));

        assertTrue(body.containsKey("status"));
        assertEquals(404, body.get("status"));

        assertTrue(body.containsKey("timestamp"));
        assertNotNull(body.get("timestamp")); // Check if timestamp exists and is not null
        // Further validation for timestamp format could be added if needed
    }

    @Test
    void handleRuntimeShouldUseExceptionMessageAsTitle() {
        String expectedMessage = "Item nao encontrado.";
        RuntimeException ex = new RuntimeException(expectedMessage);
        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertNotNull(response.getBody());
        assertEquals(expectedMessage, response.getBody().get("title"));
    }
}
