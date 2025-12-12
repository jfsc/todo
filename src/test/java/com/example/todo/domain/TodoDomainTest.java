package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoDomainTest {

    @Test
    void testConstructorsAndGetters() {
        Todo t1 = new Todo();
        assertNotNull(t1.getId());

        Todo t2 = new Todo("title", "desc");
        assertEquals("title", t2.getTitle());
        assertEquals("desc", t2.getDescription());

        UUID id = UUID.randomUUID();
        Todo t3 = new Todo(id, "hello", true);
        assertEquals(id, t3.getId());
        assertTrue(t3.isDone());
    }

    @Test
    void testSetters() {
        Todo t = new Todo();
        t.setTitle("A");
        t.setDescription("B");
        t.setDone(true);

        assertEquals("A", t.getTitle());
        assertEquals("B", t.getDescription());
        assertTrue(t.isDone());
    }
}
