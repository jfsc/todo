package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTodoRepositoryTest {

    private InMemoryTodoRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryTodoRepository();
    }

    @Test
    void shouldCreateTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "Creation Test", false);
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getTitle());
    }

    @Test
    void shouldListTodos() {
        Todo t1 = new Todo(UUID.randomUUID(), "Task 1", "description 1", false);
        Todo t2 = new Todo(UUID.randomUUID(), "Task 2", "description 2", true);

        repository.save(t1);
        repository.save(t2);

        List<Todo> list = repository.findAll();

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(t -> t.getTitle().equals("Task 1")));
        assertTrue(list.stream().anyMatch(t -> t.getTitle().equals("Task 2")));
    }


    @Test
    void shouldUpdateTodo() throws InterruptedException {
        Todo todo = new Todo(UUID.randomUUID(), "Original", "description", false);
        repository.save(todo);

        Instant oldUpdatedAt = todo.getUpdatedAt();

        todo.setTitle("Update");
        todo.setDone(true);

        Thread.sleep(10);

        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals("Update", found.get().getTitle());
        assertTrue(found.get().isDone());

        assertTrue(found.get().getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "For delete", "description", false);
        repository.save(todo);

        repository.deleteById(todo.getId());

        Optional<Todo> found = repository.findById(todo.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void shouldCreateTodoWithFullConstructor() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title", "Description", false);

        assertEquals(id, todo.getId());
        assertEquals("Title", todo.getTitle());
        assertEquals("Description", todo.getDescription());
        assertFalse(todo.isDone());
    }
}