package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    private TodoService service;

    @BeforeEach
    void setup() {
        service = new TodoService(new InMemoryTodoRepository());
    }

    @Test
    void testCreateAndList() {
        service.create(new Todo("A", "B"));
        assertEquals(1, service.list().size());
    }

    @Test
    void testFind() {
        Todo t = service.create(new Todo("A", "B"));

        assertTrue(service.find(t.getId()).isPresent());
    }

    @Test
    void testUpdate() {
        Todo t = service.create(new Todo("A", "B"));

        Todo updated = service.update(t.getId(), new Todo("C", "D"));

        assertEquals("C", updated.getTitle());
    }

    @Test
    void testDelete() {
        Todo t = service.create(new Todo("A", "B"));
        service.delete(t.getId());

        assertTrue(service.list().isEmpty());
    }
}
