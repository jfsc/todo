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

class TodoServiceTest {

    private InMemoryTodoRepository repo;
    private TodoService service;

    @BeforeEach
    void setup() {
        repo = new InMemoryTodoRepository();
        service = new TodoService(repo);
    }

    @Test
    void shouldCreateTodoAndPersistIt() {
        Todo toCreate = new Todo("Title", "Description"); // id vem random no construtor

        Todo created = service.create(toCreate);

        assertNotNull(created.getId());
        assertEquals("Title", created.getTitle());
        assertEquals("Description", created.getDescription());
        assertFalse(created.isDone());

        assertNotNull(created.getCreatedAt());
        assertNotNull(created.getUpdatedAt());

        Optional<Todo> fromRepo = repo.findById(created.getId());
        assertTrue(fromRepo.isPresent());
        assertEquals(created.getId(), fromRepo.get().getId());
    }

    @Test
    void shouldFindExistingTodoById() {
        Todo saved = repo.save(new Todo("Find me", "Desc"));

        Optional<Todo> found = service.find(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Find me", found.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenTodoNotFound() {
        Optional<Todo> found = service.find(UUID.randomUUID());
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldListAllTodos() {
        repo.save(new Todo("Task 1", "Desc 1"));
        repo.save(new Todo("Task 2", "Desc 2"));

        List<Todo> todos = service.list();

        assertEquals(2, todos.size());
        boolean hasTask1 = todos.stream().anyMatch(t -> t.getTitle().equals("Task 1"));
        boolean hasTask2 = todos.stream().anyMatch(t -> t.getTitle().equals("Task 2"));
        assertTrue(hasTask1);
        assertTrue(hasTask2);
    }

    @Test
    void shouldUpdateTodoFieldsAndKeepIdAndCreatedAt() throws InterruptedException {
        Todo original = new Todo("Original", "Old desc");
        original = repo.save(original);

        UUID id = original.getId();
        Instant originalCreatedAt = original.getCreatedAt();
        Instant originalUpdatedAt = original.getUpdatedAt();

        Thread.sleep(5);

        Todo updateData = new Todo(id, "Updated", "New desc", true);

        Todo updated = service.update(id, updateData);

        assertEquals(id, updated.getId());
        assertEquals("Updated", updated.getTitle());
        assertEquals("New desc", updated.getDescription());
        assertTrue(updated.isDone());

        assertEquals(originalCreatedAt, updated.getCreatedAt());
        assertNotNull(updated.getUpdatedAt());
        assertTrue(updated.getUpdatedAt().isAfter(originalUpdatedAt)
                || updated.getUpdatedAt().equals(originalUpdatedAt));

        Optional<Todo> fromRepo = repo.findById(id);
        assertTrue(fromRepo.isPresent());
        assertEquals("Updated", fromRepo.get().getTitle());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingTodo() {
        UUID nonExistingId = UUID.randomUUID();
        Todo updateData = new Todo(nonExistingId, "Updated", "New desc", true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.update(nonExistingId, updateData));

        assertEquals("Not found", ex.getMessage());
    }

    @Test
    void shouldDeleteTodo() {
        Todo saved = repo.save(new Todo("To delete", "Desc"));

        service.delete(saved.getId());

        Optional<Todo> found = repo.findById(saved.getId());
        assertTrue(found.isEmpty());
    }
}
