package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

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
    void testConstructorWithoutDescription() {
        Todo todo = new Todo("Title", "Desc");

        assertNotNull(todo.getId());
        assertEquals("Title", todo.getTitle());
        assertEquals("Desc", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void testConstructorWithoutDescriptionButWithDoneFlag() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title", true);

        assertEquals(id, todo.getId());
        assertEquals("Title", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void testGettersAndSetters() {
        Todo todo = new Todo();
        UUID id = UUID.randomUUID();

        todo.setId(id);
        todo.setTitle("New Title");
        todo.setDescription("New Desc");
        todo.setDone(true);

        assertEquals(id, todo.getId());
        assertEquals("New Title", todo.getTitle());
        assertEquals("New Desc", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void testCreatedAtAndUpdatedAt() {
        Todo todo = new Todo();
        Instant now = Instant.now();

        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);

        assertEquals(now, todo.getCreatedAt());
        assertEquals(now, todo.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id1 = UUID.randomUUID();

        Todo todo1 = new Todo(id1, "Title1", "Desc1", false);
        Todo todo2 = new Todo(id1, "Different", "Different", true);

        // Igualdade baseada apenas no id
        assertEquals(todo1, todo2);
        assertEquals(todo1.hashCode(), todo2.hashCode());
    }

    @Test
    void testNotEquals() {
        Todo todo1 = new Todo(UUID.randomUUID(), "A", "A", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "B", "B", true);

        assertNotEquals(todo1, todo2);
    }

    @Test
    void testEqualsWithSameObject() {
        Todo todo = new Todo();
        assertEquals(todo, todo);
    }

    @Test
    void testEqualsWithNull() {
        Todo todo = new Todo();
        assertNotEquals(todo, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        Todo todo = new Todo();
        assertNotEquals(todo, "not a todo");
    }
}
