package com.example.todo.repository.Domain;


import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TodoTest {
    @Test
    void testFullConstructor() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title", "Desc", true);

        assertEquals(id, todo.getId());
        assertEquals("Title", todo.getTitle());
        assertEquals("Desc", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void testEmptyConstructor() {
        Todo todo = new Todo();

        assertNotNull(todo.getId());
        assertEquals("", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void testConstructorWithoutId() {
        Todo todo = new Todo("Title", "Desc");

        assertNotNull(todo.getId());
        assertEquals("Title", todo.getTitle());
        assertEquals("Desc", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void testConstructorWithIdTitleAndDone() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title", true);

        assertEquals(id, todo.getId());
        assertEquals("Title", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void testSettersAndGetters() {
        Todo todo = new Todo();
        UUID newId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        todo.setId(newId);
        todo.setTitle("New Title");
        todo.setDescription("New Desc");
        todo.setDone(true);
        todo.setCreatedAt(createdAt);
        todo.setUpdatedAt(updatedAt);

        assertEquals(newId, todo.getId());
        assertEquals("New Title", todo.getTitle());
        assertEquals("New Desc", todo.getDescription());
        assertTrue(todo.isDone());
        assertEquals(createdAt, todo.getCreatedAt());
        assertEquals(updatedAt, todo.getUpdatedAt());
    }
}
