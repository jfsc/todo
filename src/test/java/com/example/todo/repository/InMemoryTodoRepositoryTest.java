package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
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
    void shouldListTodos(){
        Todo todo1 = new Todo(UUID.randomUUID(), "Todo 1", "descrição 1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Todo 2", "descrição 2", true);

        repository.save(todo1);
        repository.save(todo2);

        List<Todo> todos = repository.findAll();

        assertEquals(2, todos.size());
        assertTrue(todos.contains(todo1));
        assertTrue(todos.contains(todo2));
    }

    @Test
    void shouldUpdateTodo() throws InterruptedException {
        Todo todo = new Todo(UUID.randomUUID(), "Old Title", "desc", false);
        repository.save(todo);

        Instant firstUpdate = todo.getUpdatedAt();
        Thread.sleep(5);

        todo.setTitle("New Title");
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals("New Title", found.get().getTitle());
        assertNotEquals(firstUpdate, found.get().getUpdatedAt());
    }


    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "To Delete", "desc", false);
        repository.save(todo);

        repository.deleteById(todo.getId());

        Optional<Todo> found = repository.findById(todo.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void shouldAssignIdWhenSavingTodoWithoutId() {
        Todo todo = new Todo();
        todo.setId(null);
        todo.setTitle("No Id");
        todo.setDescription("created without id");

        Todo saved = repository.save(todo);

        assertNotNull(saved.getId());
        Optional<Todo> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("No Id", found.get().getTitle());
    }
}