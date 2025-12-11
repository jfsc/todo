package com.example.todo.repository;

import com.example.todo.domain.Todo;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todo);

        List<Todo>lista =  repository.findAll();
        assertEquals(true, !lista.isEmpty());

    }


    @Test
    void shouldUpdateTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao teste 1", false);
        repository.save(todo);

        todo.setTitle("teste teste teste");
        todo.setDescription("teste");
        todo.setDone(true);
        repository.save(todo);

        Todo todoUpdate = repository.findById(todo.getId()).orElseThrow();

        assertEquals("teste teste teste", todoUpdate.getTitle());
        assertEquals("teste", todoUpdate.getDescription());
        assertTrue( todoUpdate.isDone());

    }

    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao teste 1", false);
        repository.save(todo);

        repository.deleteById(todo.getId());

        Optional<Todo> found = repository.findById(todo.getId());
        assertFalse(found.isPresent());

    }
}