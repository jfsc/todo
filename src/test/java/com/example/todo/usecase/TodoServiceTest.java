package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    private TodoService service;
    private InMemoryTodoRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryTodoRepository();
        service = new TodoService(repo);
    }

    @Test
    void testCreate() {
        Todo t = new Todo("Title", "Desc");
        Todo saved = service.create(t);

        assertNotNull(saved.getId());
        assertEquals("Title", saved.getTitle());
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void testFind() {
        Todo t = new Todo("A", "B");
        repo.save(t);

        Optional<Todo> result = service.find(t.getId());

        assertTrue(result.isPresent());
        assertEquals("A", result.get().getTitle());
    }

    @Test
    void testList() {
        repo.save(new Todo("X", "Y"));
        repo.save(new Todo("I", "J"));

        List<Todo> all = service.list();
        assertEquals(2, all.size());
    }

    @Test
    void testUpdate() {
        Todo original = new Todo("Old", "OldDesc");
        repo.save(original);

        Todo update = new Todo("New", "NewDesc");
        update.setDone(true);

        Todo updated = service.update(original.getId(), update);

        assertEquals("New", updated.getTitle());
        assertEquals("NewDesc", updated.getDescription());
        assertTrue(updated.isDone());
    }

    @Test
    void testUpdateNotFound() {
        UUID fakeId = UUID.randomUUID();
        Todo update = new Todo("X", "Y");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.update(fakeId, update)
        );

        assertEquals("Not found", ex.getMessage());
    }

    @Test
    void testDelete() {
        Todo t = new Todo("A", "B");
        repo.save(t);

        service.delete(t.getId());

        assertTrue(repo.findAll().isEmpty());
    }
}
