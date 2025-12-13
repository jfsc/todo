package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Todo} domain entity.
 * These tests validate the behavior of constructors,
 * getters, setters, and timestamp handling.
 */
class TodoTest {

    @Test
    void shouldInitializeDefaultsWhenUsingNoArgsConstructor() {
        Todo todo = new Todo();

        assertNotNull(todo.getId(), "Identifier should be automatically generated");
        assertEquals("", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldCreateTodoWithAllFieldsProvided() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Sample title", "Sample description", true);

        assertEquals(id, todo.getId());
        assertEquals("Sample title", todo.getTitle());
        assertEquals("Sample description", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void shouldCreateTodoWithTitleAndDescriptionOnly() {
        Todo todo = new Todo("Task title", "Task description");

        assertNotNull(todo.getId());
        assertEquals("Task title", todo.getTitle());
        assertEquals("Task description", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldCreateTodoWithIdAndTitleOnly() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title only", true);

        assertEquals(id, todo.getId());
        assertEquals("Title only", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void shouldUpdateFieldsUsingSetters() {
        Todo todo = new Todo();

        UUID newId = UUID.randomUUID();
        todo.setId(newId);
        todo.setTitle("Updated title");
        todo.setDescription("Updated description");
        todo.setDone(true);

        assertEquals(newId, todo.getId());
        assertEquals("Updated title", todo.getTitle());
        assertEquals("Updated description", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void shouldAllowSettingCreationAndUpdateTimestamps() {
        Todo todo = new Todo();

        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(60);

        todo.setCreatedAt(createdAt);
        todo.setUpdatedAt(updatedAt);

        assertEquals(createdAt, todo.getCreatedAt());
        assertEquals(updatedAt, todo.getUpdatedAt());
    }

    @Test
    void shouldGenerateUniqueIdsForEachNewInstance() {
        Todo firstTodo = new Todo();
        Todo secondTodo = new Todo();

        assertNotEquals(firstTodo.getId(), secondTodo.getId(),
                "Each Todo instance must have a unique identifier");
    }
}
