package com.example.todo.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleRuntimeException() {
        RuntimeException exception = new RuntimeException("Test error message");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("about:blank", body.get("type"));
        assertEquals("Test error message", body.get("title"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    // @Test
    // void shouldHandleRuntimeExceptionWithDifferentMessage() {
    //     RuntimeException exception = new RuntimeException("Not found");

    //     ResponseEntity<Map<String, Object>> response = handler.handleRuntime(exception);

    //     assertNotNull(response);
    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
    //     Map<String, Object> body = response.getBody();
    //     assertNotNull(body);
    //     assertEquals("Not found", body.get("title"));
    // }

    // @Test
    // void shouldIncludeTimestampInResponse() {
    //     RuntimeException exception = new RuntimeException("Error");

    //     ResponseEntity<Map<String, Object>> response = handler.handleRuntime(exception);

    //     Map<String, Object> body = response.getBody();
    //     assertNotNull(body);
    //     assertTrue(body.containsKey("timestamp"));
    //     assertNotNull(body.get("timestamp"));
    // }
}