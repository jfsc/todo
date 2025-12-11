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
        Todo todo1 = new Todo(UUID.randomUUID(), "Test 1", "teste de criacao 1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Test 2", "teste de criacao 2", false);

        repository.save(todo1);
        repository.save(todo2);

        List<Todo> todoList = repository.findAll();
        assertNotNull(todoList);
        assertEquals(2, todoList.size());
    }


    @Test
    void shouldUpdateTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todo);

        todo.setTitle("Updated Test");
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals("Updated Test", found.get().getTitle());
    }

    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());

        repository.deleteById(todo.getId());
        found = repository.findById(todo.getId());
        assertFalse(found.isPresent());
    }
}