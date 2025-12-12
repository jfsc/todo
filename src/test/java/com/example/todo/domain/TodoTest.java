package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TodoTest {

    @Test
    void shouldCreateWithFullArgs() {
        UUID id = UUID.randomUUID();

        String title = "Full Create Test";
        String description = "Teste de Criação com Construtor Completo";
        boolean done = true;

        Todo todo = new Todo(id, title, description, done);

        assertEquals(id, todo.getId());
        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void shouldCreateWithNoArgs() {
        Todo todo = new Todo();

        assertNotNull(todo.getId());
        assertEquals("", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldCreateWithTitleAndDescription() {
        String title = "Title/Description Create Test";
        String description = "Teste de Criação com apenas o Title e Description";

        Todo todo = new Todo(title, description);

        assertNotNull(todo.getId());
        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldCreateWithoutDescription() {
        UUID id = UUID.randomUUID();
        String title = "No Description Create Test";

        Todo todo = new Todo(id, title, true);

        assertEquals(id, todo.getId());
        assertEquals(title, todo.getTitle());
        assertTrue(todo.isDone());
        assertEquals("", todo.getDescription());
    }

    @Test
    void shouldSetAndGetValues() {
        Todo todo = new Todo();
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        todo.setId(id);
        todo.setTitle("Getters/Setters Test");
        todo.setDescription("Teste dos Getters e Setters");
        todo.setDone(true);
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);

        assertEquals(id, todo.getId());
        assertEquals("Getters/Setters Test", todo.getTitle());
        assertEquals("Teste dos Getters e Setters", todo.getDescription());
        assertTrue(todo.isDone());
        assertEquals(now, todo.getCreatedAt());
        assertEquals(now, todo.getUpdatedAt());
    }

}
