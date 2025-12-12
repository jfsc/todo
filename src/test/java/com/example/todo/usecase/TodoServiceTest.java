package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TodoServiceTest {

    private TodoService service;
    private InMemoryTodoRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryTodoRepository();
        service = new TodoService(repository);
    }

    @Test
    void shouldSetCreatedAtAndUpdatedAt() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        service.create(todo);

        assertNotNull(todo.getCreatedAt());
        assertNotNull(todo.getUpdatedAt());
    }

    @Test
    void shouldNotFailWhenDeletingUngknowId() {
        UUID id = UUID.randomUUID();
        assertDoesNotThrow(() -> repository.deleteById(id));
    }

    @Test
    void shouldFindExistingTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Find Test", "desc", false);
        service.create(todo);

        Optional<Todo> found = service.find(todo.getId());

        assertNotNull(found.orElse(null));
    }

    @Test
    void shouldListTodosInService() {
        service.create(new Todo(null, "A", "a", false));
        service.create(new Todo(null, "B", "b", false));

        assertFalse(service.list().isEmpty());
        assertEquals(2, service.list().size());
    }

    @Test
    void shouldUpdateTodoInService() {
        Todo original = new Todo(null, "Original", "Old", false);
        Todo saved = service.create(original);

        Todo update = new Todo(null, "Updated", "New", true);

        Todo result = service.update(saved.getId(), update);

        assertEquals("Updated", result.getTitle());
        assertEquals("New", result.getDescription());
        assertTrue(result.isDone());
    }

    @Test
    void shouldDeleteTodoInService() {
        Todo todo = service.create(new Todo(null, "Test", "Desc", false));

        service.delete(todo.getId());

        assertTrue(service.list().isEmpty());
    }

}
