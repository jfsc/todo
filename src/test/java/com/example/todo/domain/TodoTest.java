package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Todo} domain object.  These tests exercise the
 * various constructors, getters and setters to ensure that the object
 * maintains state correctly.  By covering the different construction
 * paths and mutators we also implicitly verify that the automatically
 * generated identifiers and timestamps behave as expected.
 */
class TodoTest {

    @Test
    void testConstructorsAndGetters() {
        UUID id = UUID.randomUUID();
        // full constructor
        Todo full = new Todo(id, "Title", "Description", true);
        assertEquals(id, full.getId());
        assertEquals("Title", full.getTitle());
        assertEquals("Description", full.getDescription());
        assertTrue(full.isDone());

        // no‑arg constructor should generate an id and default fields
        Todo empty = new Todo();
        assertNotNull(empty.getId(), "Default constructor should assign an id");
        assertEquals("", empty.getTitle());
        assertEquals("", empty.getDescription());
        assertFalse(empty.isDone());

        // constructor without id
        Todo withStrings = new Todo("Short title", "Some text");
        assertNotNull(withStrings.getId());
        assertEquals("Short title", withStrings.getTitle());
        assertEquals("Some text", withStrings.getDescription());
        assertFalse(withStrings.isDone());

        // constructor with id, title and done flag
        UUID id2 = UUID.randomUUID();
        Todo partial = new Todo(id2, "Another", false);
        assertEquals(id2, partial.getId());
        assertEquals("Another", partial.getTitle());
        assertEquals("", partial.getDescription());
        assertFalse(partial.isDone());
    }

    @Test
    void testSettersUpdateState() {
        Todo t = new Todo();
        t.setTitle("My Title");
        t.setDescription("My Description");
        t.setDone(true);
        Instant now = Instant.now();
        t.setCreatedAt(now);
        t.setUpdatedAt(now);

        assertEquals("My Title", t.getTitle());
        assertEquals("My Description", t.getDescription());
        assertTrue(t.isDone());
        assertEquals(now, t.getCreatedAt());
        assertEquals(now, t.getUpdatedAt());
    }
}