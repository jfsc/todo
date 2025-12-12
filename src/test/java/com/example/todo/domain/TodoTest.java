package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void defaultConstructorShouldInitializeFields() {
        Todo todo = new Todo();

        assertNotNull(todo.getId());
        assertEquals("", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void constructorWithIdTitleAndDoneShouldInitializeFields() {
        UUID id = UUID.randomUUID();

        Todo todo = new Todo(id, "Title", true);

        assertEquals(id, todo.getId());
        assertEquals("Title", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertTrue(todo.isDone());
    }
}
