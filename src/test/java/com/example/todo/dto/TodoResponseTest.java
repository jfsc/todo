package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoResponseTest {

    @Test
    void shouldMapFromDomainCorrectly() {
        UUID id = UUID.randomUUID();
        Instant created = Instant.now();
        Instant updated = Instant.now();

        Todo todo = new Todo();
        todo.setId(id);
        todo.setTitle("Test title");
        todo.setDescription("Test description");
        todo.setDone(true);
        todo.setCreatedAt(created);
        todo.setUpdatedAt(updated);

        TodoResponse response = TodoResponse.from(todo);

        assertEquals(id, response.getId());
        assertEquals("Test title", response.getTitle());
        assertEquals("Test description", response.getDescription());
        assertTrue(response.isDone());
        assertEquals(created, response.getCreatedAt());
        assertEquals(updated, response.getUpdatedAt());
    }

    @Test
    void shouldHandleNullFields() {
        Todo todo = new Todo();
        todo.setId(null);
        todo.setTitle(null);
        todo.setDescription(null);
        todo.setCreatedAt(null);
        todo.setUpdatedAt(null);
        todo.setDone(false);

        TodoResponse response = TodoResponse.from(todo);

        assertNull(response.getId());
        assertNull(response.getTitle());
        assertNull(response.getDescription());
        assertNull(response.getCreatedAt());
        assertNull(response.getUpdatedAt());
        assertFalse(response.isDone());
    }
}