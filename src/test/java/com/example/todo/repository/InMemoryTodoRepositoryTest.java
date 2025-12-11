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
    private Todo todoTest;

    @BeforeEach
    void setup() {
        repository = new InMemoryTodoRepository();
        todoTest = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todoTest);
    }

    @Test
    void shouldCreateTodo() {

        Optional<Todo> found = repository.findById(todoTest.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getTitle());
    }


    @Test
    void shouldListTodos() {
        List<Todo> todoList = repository.findAll();

        assertFalse(todoList.isEmpty());
        assertTrue(todoList.contains(todoTest));
    }

    @Test
    void shouldUpdateTodo() {
        todoTest.setDone(true);
        repository.save(todoTest);
        Optional<Todo> todoUpdated = repository.findById(todoTest.getId());
        Todo todoAltered = null;

        if (todoUpdated.isPresent()) {
            todoAltered = todoUpdated.get();
        }

        assertNotNull(todoAltered);
        assertEquals(todoTest, todoAltered);
    }

    @Test
    void shouldDeleteTodo() {
        repository.deleteById(todoTest.getId());

        Optional<Todo> searchTodoTest = repository.findById(todoTest.getId());
        Todo todoAltered = null;

        if (searchTodoTest.isPresent()) {
            todoAltered = searchTodoTest.get();
        }

        assertNull(todoAltered);
    }
}