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
    void shouldHandleIdNullExceptionWithBadRequestStatus() {
        IdNullException ex = new IdNullException("ID cannot be null", 400);

        ResponseEntity<Map<String, Object>> response = handler.handleIdNullException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("about:blank", body.get("type"));
        assertEquals("ID cannot be null", body.get("title"));
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void shouldHandleRuntimeExceptionWithInternalServerError() {
        RuntimeException ex = new RuntimeException("Unexpected error");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("about:blank", body.get("type"));
        assertEquals("Unexpected error", body.get("title"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void shouldHandleTodoNotFoundExceptionWithNotFoundStatus() {
        TodoNotFoundException ex = new TodoNotFoundException("Todo not found");

        ResponseEntity<Map<String, Object>> response = handler.handleTodoNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("about:blank", body.get("type"));
        assertEquals("Todo not found", body.get("title"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void shouldHandleIdNullExceptionWithInternalServerErrorWhenCodeIs500() {
        IdNullException ex = new IdNullException("Unexpected error", 500);

        ResponseEntity<Map<String, Object>> response = handler.handleIdNullException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("about:blank", body.get("type"));
        assertEquals("Unexpected error", body.get("title"));
        assertEquals(500, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }
}
