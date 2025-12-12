package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void shouldCreateTodoWithFullConstructor() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Test Title", "Test Description", true);

        assertEquals(id, todo.getId());
        assertEquals("Test Title", todo.getTitle());
        assertEquals("Test Description", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void shouldCreateTodoWithDefaultConstructor() {
        Todo todo = new Todo();

        assertNotNull(todo.getId());
        assertEquals("", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldCreateTodoWithTitleAndDescription() {
        Todo todo = new Todo("My Title", "My Description");

        assertNotNull(todo.getId());
        assertEquals("My Title", todo.getTitle());
        assertEquals("My Description", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldCreateTodoWithIdTitleAndDone() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "My Title", true);

        assertEquals(id, todo.getId());
        assertEquals("My Title", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void shouldSetAndGetId() {
        Todo todo = new Todo();
        UUID newId = UUID.randomUUID();

        todo.setId(newId);

        assertEquals(newId, todo.getId());
    }

    @Test
    void shouldSetAndGetTitle() {
        Todo todo = new Todo();

        todo.setTitle("New Title");

        assertEquals("New Title", todo.getTitle());
    }

    @Test
    void shouldSetAndGetDescription() {
        Todo todo = new Todo();

        todo.setDescription("New Description");

        assertEquals("New Description", todo.getDescription());
    }

    @Test
    void shouldSetAndGetDone() {
        Todo todo = new Todo();

        todo.setDone(true);

        assertTrue(todo.isDone());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        Todo todo = new Todo();
        Instant now = Instant.now();

        todo.setCreatedAt(now);

        assertEquals(now, todo.getCreatedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        Todo todo = new Todo();
        Instant now = Instant.now();

        todo.setUpdatedAt(now);

        assertEquals(now, todo.getUpdatedAt());
    }

    @Test
    void shouldHandleNullTimestamps() {
        Todo todo = new Todo(UUID.randomUUID(), "Title", "Description", false);

        assertNull(todo.getCreatedAt());
        assertNull(todo.getUpdatedAt());
    }
}