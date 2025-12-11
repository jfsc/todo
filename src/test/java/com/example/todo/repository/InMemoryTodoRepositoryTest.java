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
        Todo newTodo = new Todo(UUID.randomUUID(), "Todo Test", "Todo test description", false);
        repository.save(newTodo);

        List<Todo> listTodos = repository.findAll();
        assertFalse(listTodos.isEmpty());
    }

    @Test
    void shouldUpdateTodo() {
        Todo newTodo = new Todo(UUID.randomUUID(), "Todo Test", "Todo test description", false);
        repository.save(newTodo);

        newTodo.setTitle("Todo Test Update");
        newTodo.setDescription("Todo test description updated");
        newTodo.setDone(true);

        repository.save(newTodo);

        Todo updated = repository.findById(newTodo.getId()).orElseThrow(() -> new RuntimeException("Not found!"));

        assertEquals("Todo Test Update", updated.getTitle());
        assertEquals("Todo test description updated", updated.getDescription());
        assertTrue(updated.isDone());
    }

    @Test
    void shouldDeleteTodo() {
        Todo newTodo = new Todo(UUID.randomUUID(), "Todo Test", "Todo test description", false);
        repository.save(newTodo);

        assertFalse(repository.findAll().isEmpty());

        repository.deleteById(newTodo.getId());

        assertTrue(repository.findAll().isEmpty());
    }

}