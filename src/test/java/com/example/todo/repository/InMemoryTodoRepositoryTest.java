package com.example.todo.repository;

import com.example.todo.domain.Todo;
import com.example.todo.exception.IdNullException;

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
    void shouldNotFindTodoWithNullId() {

        assertThrows(IdNullException.class, () -> {
            repository.findById(null);
        });
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
        Todo todo1 = new Todo(UUID.randomUUID(), "Task 1", "desc 1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Task 2", "desc 2", true);

        repository.save(todo1);
        repository.save(todo2);

        List<Todo> todos = repository.findAll();

        assertEquals(2, todos.size());

        boolean hasTodo1 = todos.stream()
                .anyMatch(t -> t.getId().equals(todo1.getId()));
        boolean hasTodo2 = todos.stream()
                .anyMatch(t -> t.getId().equals(todo2.getId()));

        assertTrue(hasTodo1);
        assertTrue(hasTodo2);
    }

    @Test
    void shouldUpdateTodo() {
        UUID id = UUID.randomUUID();
        Todo original = new Todo(id, "Original", "desc original", false);
        repository.save(original);

        Todo updated = new Todo(id, "Updated", "desc atualizada", true);
        repository.save(updated);

        Optional<Todo> found = repository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Updated", found.get().getTitle());
        assertEquals("desc atualizada", found.get().getDescription());
        assertTrue(found.get().isDone());
    }

    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "To Delete", "desc", false);
        repository.save(todo);

        repository.deleteById(todo.getId());

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldGenerateRandomIdWhenIdIsNull() {
        Todo todo = new Todo(null, "Title", "Description", false);
        assertNull(todo.getId());

        Todo saved = repository.save(todo);

        assertNotNull(saved.getId());

        Optional<Todo> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());

    }

    @Test
    void shouldGenerateDifferentIdsForDifferentTodosWithNullId() {
        Todo todo1 = new Todo(null, "Task 1", "Desc 1", false);
        Todo todo2 = new Todo(null, "Task 2", "Desc 2", false);

        repository.save(todo1);
        repository.save(todo2);

        assertNotNull(todo1.getId());
        assertNotNull(todo2.getId());
        assertNotEquals(todo1.getId(), todo2.getId());
    }

}
