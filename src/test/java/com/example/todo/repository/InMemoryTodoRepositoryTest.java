package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTodoRepositoryTest {

    private InMemoryTodoRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryTodoRepository();
    }

    @Test
    void shouldCreateTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getTitle());
    }

    @Test
    void shouldListTodos() {
        Todo todo1 = new Todo(UUID.randomUUID(), "Teste 1", "teste de criação 1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Teste 2", "teste de criação 2", true);

        repository.save(todo1);
        repository.save(todo2);

        List<Todo> todos = repository.findAll();

    }

    @Test
    void shouldUpdateTodo() {
        UUID id = UUID.randomUUID();
        Todo existingTodo = new Todo(id, "Test Title", "Test Description", false);
        repository.save(existingTodo);

        Todo updated = new Todo(id, "Updated Title", "Updated Description", true);
        repository.save(updated);

        Optional<Todo> result = repository.findById(id);
        assertTrue(result.isPresent());
        assertEquals("Updated Title", result.get().getTitle());
    }

    @Test
    void shouldDeleteTodo() {
        UUID id = UUID.randomUUID();
        Todo existingTodo = new Todo(id, "To Delete", "Will be deleted", false);
        repository.save(existingTodo);

        assertTrue(repository.findById(id).isPresent());
        repository.deleteById(id);

        Optional<Todo> result = repository.findById(id);
        assertFalse(result.isPresent());
    }

}