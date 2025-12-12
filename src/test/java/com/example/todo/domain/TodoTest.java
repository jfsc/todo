package com.example.todo.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    private final UUID FIXED_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final Instant FIXED_TIME = Instant.parse("2025-12-12T10:00:00Z");

    @Test
    void fullConstructor_shouldHandleNullsAndGenerateDefaults() {
        Todo todo = new Todo(null, null, null, false, null, null);

        assertNotNull(todo.getId(), "ID deve ser gerado se for null.");
        assertTrue(todo.getTitle().isEmpty(), "Título deve ser string vazia se for null.");
        assertTrue(todo.getDescription().isEmpty(), "Descrição deve ser string vazia se for null.");
        assertNotNull(todo.getCreatedAt(), "createdAt deve ser Instant.now() se for null.");
    }

    @Test
    void fullConstructor_shouldUseProvidedValues() {
        Todo todo = new Todo(FIXED_ID, "Title", "Desc", true, FIXED_TIME, FIXED_TIME);

        assertEquals(FIXED_ID, todo.getId());
        assertEquals("Title", todo.getTitle());
        assertTrue(todo.isDone());
        assertEquals(FIXED_TIME, todo.getCreatedAt());
        assertEquals(FIXED_TIME, todo.getUpdatedAt());
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaults() {
        Todo todo = new Todo();

        assertNotNull(todo.getId());
        assertTrue(todo.getTitle().isEmpty());
        assertFalse(todo.isDone());
    }

    @Test
    void titleDescriptionConstructor_shouldInitializeCorrectly() {
        Todo todo = new Todo("New Title", "New Desc");

        assertEquals("New Title", todo.getTitle());
        assertEquals("New Desc", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void idTitleDoneConstructor_shouldInitializeCorrectly() {
        Todo todo = new Todo(FIXED_ID, "Final Title", true);

        assertEquals(FIXED_ID, todo.getId());
        assertEquals("Final Title", todo.getTitle());
        assertTrue(todo.isDone());
        assertTrue(todo.getDescription().isEmpty());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        Todo todo = new Todo();
        Instant newTime = Instant.now().plusSeconds(3600);
        UUID newId = UUID.randomUUID();

        todo.setId(newId);
        todo.setTitle("Updated Title");
        todo.setDescription("Updated Description");
        todo.setDone(true);
        todo.setCreatedAt(FIXED_TIME);
        todo.setUpdatedAt(newTime);

        assertEquals(newId, todo.getId());
        assertEquals("Updated Title", todo.getTitle());
        assertEquals("Updated Description", todo.getDescription());
        assertTrue(todo.isDone());
        assertEquals(FIXED_TIME, todo.getCreatedAt());
        assertEquals(newTime, todo.getUpdatedAt());
    }
}