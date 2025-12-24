package com.example.todo.domain;

import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void testDefaultConstructor(){
        Todo td = new Todo();

        assertNotNull(td.getId(), "UUID should be generated");
        assertEquals("", td.getTitle(), "Title should be empty string");
        assertEquals("", td.getDescription(), "Description should be empty string");
        assertFalse(td.isDone(), "Done should be false by default");
    }

    @Test
    void testFullConstructor(){
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", true);

        assertEquals(todo.getId(), todo.getId());
        assertEquals("Test", todo.getTitle());
        assertEquals("teste de criacao", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void testTitleDescriptionConstructor() {
        Todo todo = new Todo("Test 2", "Teste 2");

        assertNotNull(todo.getId());
        assertEquals("Test 2", todo.getTitle());
        assertEquals("Teste 2", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void testIdTitleDoneConstructor() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Teste 3", true);

        assertEquals(id, todo.getId());
        assertEquals("Teste 3", todo.getTitle());
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