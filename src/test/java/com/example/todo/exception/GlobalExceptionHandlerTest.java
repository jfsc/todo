package com.example.todo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void handleRuntime_shouldReturn404BodyWithMessage() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        RuntimeException ex = new RuntimeException("Not found");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Not found", body.get("title"));
        assertEquals(404, body.get("status"));
        assertTrue(body.containsKey("timestamp"));

        assertDoesNotThrow(() -> Instant.parse((CharSequence) body.get("timestamp")));
    }
}
