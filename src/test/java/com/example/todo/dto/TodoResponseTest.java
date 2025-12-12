package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoResponseTest {

    @Test
    void testFrom_ShouldCopyAllFields() {
        UUID id = UUID.randomUUID();
        Instant created = Instant.now();
        Instant updated = Instant.now();

        Todo todo = new Todo();
        todo.setId(id);
        todo.setTitle("Test Title");
        todo.setDescription("Test Description");
        todo.setDone(true);
        todo.setCreatedAt(created);
        todo.setUpdatedAt(updated);

        TodoResponse response = TodoResponse.from(todo);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Description", response.getDescription());
        assertTrue(response.isDone());
        assertEquals(created, response.getCreatedAt());
        assertEquals(updated, response.getUpdatedAt());
    }

    @Test
    void testFrom_WhenTodoIsNull_ShouldReturnNull() {
        TodoResponse response = TodoResponse.from(null);
        assertNull(response);
    }
}
