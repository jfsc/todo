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
        Todo t1 = new Todo(UUID.randomUUID(), "A", "desc A", false);
        Todo t2 = new Todo(UUID.randomUUID(), "B", "desc B", true);

        repository.save(t1);
        repository.save(t2);

        List<Todo> todos = repository.findAll();
        assertEquals(2, todos.size());

        assertTrue(todos.stream().anyMatch(t -> t.getTitle().equals("A")));
        assertTrue(todos.stream().anyMatch(t -> t.getTitle().equals("B")));
    }

    @Test
    void shouldUpdateTodo() throws InterruptedException {
        Todo todo = new Todo(UUID.randomUUID(), "Old Title", "Old desc", false);
        repository.save(todo);

        var oldUpdatedAt = todo.getUpdatedAt();

        Thread.sleep(5);

        todo.setTitle("New Title");
        todo.setDescription("New description");
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());

        assertEquals("New Title", found.get().getTitle());
        assertEquals("New description", found.get().getDescription());

        assertEquals(todo.getCreatedAt(), found.get().getCreatedAt());

        assertTrue(found.get().getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Delete me", "desc", false);
        repository.save(todo);

        repository.deleteById(todo.getId());

        Optional<Todo> result = repository.findById(todo.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGenerateIdWhenSavingTodoWithoutId() {
        Todo todo = new Todo(null, "AutoID", "desc", false);
        todo.setCreatedAt(null); // para testar também o createdAt == null

        Todo saved = repository.save(todo);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldReturnEmptyListWhenNoTodosSaved() {
        List<Todo> todos = repository.findAll();
        assertTrue(todos.isEmpty());
    }

    @Test
    void shouldNotFailWhenDeletingNonExistingId() {
        UUID randomId = UUID.randomUUID();

        // não deve lançar exceção
        assertDoesNotThrow(() -> repository.deleteById(randomId));

        // e a store continua vazia
        assertTrue(repository.findAll().isEmpty());
    }

}