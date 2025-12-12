package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoRequestTest {

    @Test
    void testToDomain() {
        TodoRequest req = new TodoRequest();
        req.setTitle("Hello");
        req.setDescription("World");
        req.setDone(true);

        Todo t = req.toDomain();

        assertEquals("Hello", t.getTitle());
        assertEquals("World", t.getDescription());
        assertTrue(t.isDone());
    }
}
