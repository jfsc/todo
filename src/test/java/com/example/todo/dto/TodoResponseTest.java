package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoResponseTest {

    @Test
    void testFromDomain() {
        Todo t = new Todo("A", "B");
        t.setDone(true);

        TodoResponse r = TodoResponse.from(t);

        assertEquals(t.getId(), r.getId());
        assertEquals("A", r.getTitle());
        assertEquals("B", r.getDescription());
        assertTrue(r.isDone());
    }
}
