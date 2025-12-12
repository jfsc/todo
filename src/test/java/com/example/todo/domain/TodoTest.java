package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void testDefaultConstructor() {
        Todo todo = new Todo();

        assertNotNull(todo.getId(), "UUID should be generated");
        assertEquals("", todo.getTitle(), "Title should be empty string");
        assertEquals("", todo.getDescription(), "Description should be empty string");
        assertFalse(todo.isDone(), "Done should be false by default");
    }

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
    void testTitleDescriptionConstructor() {
        Todo todo = new Todo("Title2", "Desc2");

        assertNotNull(todo.getId());
        assertEquals("Title2", todo.getTitle());
        assertEquals("Desc2", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void testIdTitleDoneConstructor() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title3", true);

        assertEquals(id, todo.getId());
        assertEquals("Title3", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void testSettersAndGetters() {
        Todo todo = new Todo();

        UUID newId = UUID.randomUUID();
        todo.setId(newId);
        todo.setTitle("New Title");
        todo.setDescription("New Desc");
        todo.setDone(true);
        Instant now = Instant.now();
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);

        assertEquals(newId, todo.getId());
        assertEquals("New Title", todo.getTitle());
        assertEquals("New Desc", todo.getDescription());
        assertTrue(todo.isDone());
        assertEquals(now, todo.getCreatedAt());
        assertEquals(now, todo.getUpdatedAt());
    }
}
