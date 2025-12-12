package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTodoDomainTest {
    @Test
    void shouldCreateTodoWithFullConstructor() {
        UUID id = UUID.randomUUID();
        String title = "Title";
        String desc = "Description";
        boolean done = true;

        Todo todo = new Todo(id, title, desc, done);

        assertEquals(id, todo.getId());
        assertEquals(title, todo.getTitle());
        assertEquals(desc, todo.getDescription());
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
    void shouldCreateTodoWithTitleAndDescriptionConstructor() {
        Todo todo = new Todo("Task", "Desc");

        assertNotNull(todo.getId());
        assertEquals("Task", todo.getTitle());
        assertEquals("Desc", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldCreateTodoWithIdTitleDoneConstructor() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Task", true);

        assertEquals(id, todo.getId());
        assertEquals("Task", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void shouldUpdateAllFields() {
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
