package com.example.todo.service;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTodoServiceTest {
    private TodoService service;
    private InMemoryTodoRepository repo;

    @BeforeEach
    void setup() {
        repo = new InMemoryTodoRepository();
        service = new TodoService(repo);
    }

    @Test
    void createShouldSaveTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "title", "desc", false);

        Todo result = service.create(todo);

        assertNotNull(result);
        assertEquals("title", result.getTitle());
        assertEquals(1, service.list().size());
    }

    @Test
    void findShouldReturnTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "title", "desc", false);
        repo.save(todo);

        var result = service.find(todo.getId());

        assertTrue(result.isPresent());
        assertEquals(todo.getId(), result.get().getId());
    }

    @Test
    void findShouldReturnEmptyWhenNotFound() {
        var result = service.find(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void listShouldReturnAllTodos() {
        repo.save(new Todo(UUID.randomUUID(), "a", "b", false));
        repo.save(new Todo(UUID.randomUUID(), "c", "d", true));

        List<Todo> list = service.list();

        assertEquals(2, list.size());
    }

    @Test
    void updateShouldModifyExistingTodo() {
        Todo original = new Todo(UUID.randomUUID(), "old", "old-desc", false);
        repo.save(original);

        Todo update = new Todo(null, "new", "new-desc", true);

        Todo result = service.update(original.getId(), update);

        assertEquals("new", result.getTitle());
        assertEquals("new-desc", result.getDescription());
        assertTrue(result.isDone());
    }

    @Test
    void updateShouldThrowWhenNotFound() {
        Todo update = new Todo(null, "x", "y", true);

        assertThrows(RuntimeException.class, () ->
                service.update(UUID.randomUUID(), update)
        );
    }

    @Test
    void deleteShouldRemoveTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "title", "desc", false);
        repo.save(todo);

        service.delete(todo.getId());

        assertTrue(service.list().isEmpty());
    }
}