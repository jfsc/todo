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
        Todo todo1 = new Todo(UUID.randomUUID(), "A", "descricao A", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "B", "descricao B", true);

        repository.save(todo1);
        repository.save(todo2);

        List<Todo> todos = repository.findAll();

        assertEquals(2, todos.size());
        assertTrue(todos.contains(todo1));
        assertTrue(todos.contains(todo2));
    }

    @Test
    void shouldUpdateTodo() {
        UUID id = UUID.randomUUID();
        Todo original = new Todo(id, "Original", "descricao teste", false);
        repository.save(original);

        Todo updated = new Todo(id, "Atualizado", "nova desceicao teste", true);
        repository.update(id, updated);

        Optional<Todo> found = repository.findById(id);

        assertTrue(found.isPresent());
        assertEquals("Atualizado", found.get().getTitle());
        assertEquals("nova desceicao teste", found.get().getDescription());
        assertTrue(found.get().isDone());
    }

    @Test
    void shouldDeleteTodo() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Deletar", "deletar", false);

        repository.save(todo);
        repository.deleteById(id);

        Optional<Todo> found = repository.findById(id);

        assertTrue(found.isEmpty(), "O todo deveria ter sido deletado");
    }
}