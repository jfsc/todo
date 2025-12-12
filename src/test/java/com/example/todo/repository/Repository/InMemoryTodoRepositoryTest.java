package com.example.todo.repository.Repository;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
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
    void shouldCreateTodoWithoutId() {
        Todo todo = new Todo();
        todo.setId(null);
        todo.setTitle("No ID");
        todo.setDescription("Test without ID");

        Todo saved = repository.save(todo);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldCreateTodoWithoutCreatedAt() {
        Todo todo = new Todo();
        todo.setId(UUID.randomUUID());
        todo.setTitle("No createdAt");

        Todo saved = repository.save(todo);

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldUpdateTodo() {
        Todo todo = new Todo();
        todo.setTitle("Test");
        Todo saved = repository.save(todo);

        Instant originalCreatedAt = saved.getCreatedAt();

        saved.setTitle("Updated");
        saved.setDescription("Updated description");
        Todo updated = repository.save(saved);

        assertEquals("Updated", updated.getTitle());
        assertEquals("Updated description", updated.getDescription());
        assertEquals(originalCreatedAt, updated.getCreatedAt()); // createdAt must not change
        assertNotNull(updated.getUpdatedAt());
    }



    @Test
    void shouldFindById() {
        Todo todo = new Todo();
        todo.setTitle("Test");
        Todo saved = repository.save(todo);

        Optional<Todo> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenIdNotFound() {
        Optional<Todo> found = repository.findById(UUID.randomUUID());
        assertTrue(found.isEmpty());
    }



    @Test
    void shouldListTodos() {
        repository.save(new Todo());
        repository.save(new Todo());
        repository.save(new Todo());

        List<Todo> todos = repository.findAll();

        assertEquals(3, todos.size());
    }


    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo();
        repository.save(todo);

        repository.deleteById(todo.getId());

        Optional<Todo> found = repository.findById(todo.getId());
        assertFalse(found.isPresent());
    }
}
