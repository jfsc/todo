package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoResponseTest {

    @Test
    void fromShouldMapAllFields() {
        UUID id = UUID.randomUUID();
        Instant created = Instant.now();
        Instant updated = created.plusSeconds(10);

        Todo todo = new Todo(id, "Title", "Desc", true);
        todo.setCreatedAt(created);
        todo.setUpdatedAt(updated);

        TodoResponse resp = TodoResponse.from(todo);

        assertEquals(id, resp.getId());
        assertEquals("Title", resp.getTitle());
        assertEquals("Desc", resp.getDescription());
        assertTrue(resp.isDone());
        assertEquals(created, resp.getCreatedAt());
        assertEquals(updated, resp.getUpdatedAt());
    }
}
