package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

class TodoServiceTest {

    private InMemoryTodoRepository repo;
    private TodoService service;

    @BeforeEach
    void setup() {
        repo = mock(InMemoryTodoRepository.class);
        service = new TodoService(repo);
    }

    @Test
    void shouldCreateTodo() {
        Todo todo = new Todo(null, "Test", "teste de criacao", false);
        Todo savedTodo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);

        when(repo.save(any(Todo.class))).thenReturn(savedTodo);

        Todo result = service.create(todo);

        assertEquals(savedTodo, result);
        verify(repo).save(any(Todo.class));
    }

    @Test
    void shouldReturnById() {
        UUID id = UUID.randomUUID();
        Todo mockTodo = new Todo(id, "Title", "Description", false);

        when(repo.findById(id)).thenReturn(Optional.of(mockTodo));

        Optional<Todo> result = service.find(id);

        assertTrue(result.isPresent());
        assertEquals(mockTodo, result.get());

        verify(repo).findById(id);
    }

    @Test
    void shouldList() {
        Todo todo1 = new Todo(UUID.randomUUID(), "Todo 1", "descrição 1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Todo 2", "descrição 2", true);

        when(repo.findAll()).thenReturn(List.of(todo1, todo2));

        List<Todo> todos = service.list();

        assertEquals(2, todos.size());
        assertTrue(todos.contains(todo1));
        assertTrue(todos.contains(todo2));

        verify(repo).findAll();
    }

    @Test
    void shouldUpdateTodo() {
        UUID id = UUID.randomUUID();
        Todo existing = new Todo(id, "Old Title", "desc", false);
        existing.setUpdatedAt(Instant.now());

        Todo updateData = new Todo(id, "New Title", "desc", false);

        Instant firstUpdate = existing.getUpdatedAt();

        when(repo.findById(id)).thenReturn(Optional.of(existing));

        when(repo.save(any(Todo.class))).thenAnswer(invocation -> {
            Todo t = invocation.getArgument(0);
            t.setUpdatedAt(Instant.now());
            return t;
        });

        Todo updated = service.update(id, updateData);

        assertEquals("New Title", updated.getTitle());
        assertTrue(updated.getUpdatedAt().isAfter(firstUpdate));

        verify(repo).findById(id);
        verify(repo).save(any(Todo.class));
    }

    @Test
    void shouldDeleteTodo() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "To Delete", "desc", false);

        when(repo.findById(id)).thenReturn(Optional.of(todo));

        service.delete(id);

        verify(repo).deleteById(id);
    }
}
