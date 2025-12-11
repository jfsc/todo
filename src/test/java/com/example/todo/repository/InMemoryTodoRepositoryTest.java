package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Todo todo1 = new Todo(UUID.randomUUID(), "Todo 1", "First todo", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Todo 2", "Second todo", true);
        Todo todo3 = new Todo(UUID.randomUUID(), "Todo 3", "Third todo", false);

        repository.save(todo1);
        repository.save(todo2);
        repository.save(todo3);

        List<Todo> todos = repository.findAll();

        assertEquals(3, todos.size());
    }

    @Test
    void shouldUpdateTodo() {
        UUID todoId = UUID.randomUUID();
        Todo todo = new Todo(todoId, "Original Title", "Original description", false);
        repository.save(todo);

        Instant createdAt = todo.getCreatedAt();

        todo.setTitle("Updated Title");
        todo.setDescription("Updated description");
        todo.setDone(true);

        repository.save(todo);

        Optional<Todo> updated = repository.findById(todoId);
        assertTrue(updated.isPresent());
        assertEquals("Updated Title", updated.get().getTitle());
        assertEquals("Updated description", updated.get().getDescription());
        assertTrue(updated.get().isDone());
        assertEquals(createdAt, updated.get().getCreatedAt());
        assertNotNull(updated.get().getUpdatedAt());
    }

    @Test
    void shouldDeleteTodo() {
        UUID todoId = UUID.randomUUID();
        Todo todo = new Todo(todoId, "Todo to Delete", "Will be deleted", false);
        repository.save(todo);

        Optional<Todo> found = repository.findById(todoId);
        assertTrue(found.isPresent());

        repository.deleteById(todoId);

        Optional<Todo> notFound = repository.findById(todoId);
        assertFalse(notFound.isPresent());
    }
}