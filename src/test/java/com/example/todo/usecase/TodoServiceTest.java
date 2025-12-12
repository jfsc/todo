package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TodoService}.  These tests exercise the
 * create, find, list, update and delete behaviours of the service and
 * verify that the service correctly delegates to its underlying
 * repository while enforcing business rules such as raising an exception
 * when attempting to update or delete a non‑existent Todo.
 */
class TodoServiceTest {
    private TodoService service;

    @BeforeEach
    void setUp() {
        service = new TodoService(new InMemoryTodoRepository());
    }

    @Test
    void createShouldPersistTodoWithTimestamps() {
        Todo t = new Todo("Service test", "Testing create");
        Todo created = service.create(t);
        assertNotNull(created.getId());
        assertNotNull(created.getCreatedAt(), "Created At should be set by repository");
        assertNotNull(created.getUpdatedAt());
    }

    @Test
    void findShouldReturnOptionalWhenPresent() {
        Todo t = new Todo("Find test", "Testing find");
        Todo created = service.create(t);
        Optional<Todo> found = service.find(created.getId());
        assertTrue(found.isPresent());
        assertEquals("Find test", found.get().getTitle());
    }

    @Test
    void listShouldReturnAllTodos() {
        Todo t1 = new Todo("One", "Desc1");
        Todo t2 = new Todo("Two", "Desc2");
        service.create(t1);
        service.create(t2);
        List<Todo> list = service.list();
        assertEquals(2, list.size());
    }

    @Test
    void updateShouldModifyExistingTodo() {
        Todo t = new Todo("Update me", "Old desc");
        Todo created = service.create(t);
        Todo update = new Todo("Updated", "New desc");
        update.setDone(true);
        Todo updated = service.update(created.getId(), update);
        assertEquals("Updated", updated.getTitle());
        assertEquals("New desc", updated.getDescription());
        assertTrue(updated.isDone());
    }

    @Test
    void updateShouldThrowWhenTodoNotFound() {
        Todo update = new Todo("Doesn't matter", "No desc");
        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> service.update(id, update));
    }

    @Test
    void deleteShouldRemoveExistingTodo() {
        Todo t = new Todo("Delete test", "Testing delete");
        Todo created = service.create(t);
        service.delete(created.getId());
        assertTrue(service.find(created.getId()).isEmpty());
    }

    @Test
    void deleteShouldThrowWhenTodoNotFound() {
        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> service.delete(id));
    }
}