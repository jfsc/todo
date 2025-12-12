package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoRequestTest {

    @Test
    void gettersAndSettersShouldWork() {
        TodoRequest req = new TodoRequest();

        req.setTitle("Title");
        req.setDescription("Desc");
        req.setDone(true);

        assertEquals("Title", req.getTitle());
        assertEquals("Desc", req.getDescription());
        assertTrue(req.isDone());
    }

    @Test
    void toDomainShouldMapFields() {
        TodoRequest req = new TodoRequest();
        req.setTitle("Title");
        req.setDescription("Desc");
        req.setDone(true);

        Todo todo = req.toDomain();

        assertNotNull(todo.getId());
        assertEquals("Title", todo.getTitle());
        assertEquals("Desc", todo.getDescription());
        assertTrue(todo.isDone());
    }
}
