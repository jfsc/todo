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

    // --- Seus Testes Originais (CRUD de Sucesso) ---

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
        Todo todo1 = new Todo(UUID.randomUUID(), "Test1", "teste de criacao 1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Test2", "teste de criacao 2", false);
        repository.save(todo1);
        repository.save(todo2);

        List<Todo> todos = repository.findAll();
        assertEquals(2, todos.size());
    }

    @Test
    void shouldUpdateTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todo);

        todo.setTitle("Updated Test");
        todo.setDescription(("updated description"));
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals("Updated Test", found.get().getTitle());
    }

    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "testede criacao", false);
        repository.save(todo);
        repository.deleteById(todo.getId());

        Optional<Todo> found = repository.findById(todo.getId());
        assertFalse(found.isPresent());
    }


    @Test
    void shouldGenerateIdWhenNull() {
        Todo todo = new Todo(null, "Test", "desc", false);
        Todo saved = repository.save(todo);

        assertNotNull(saved.getId());
    }

    @Test
    void shouldSetCreatedAtWhenNull() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "desc", false);

        // Garante que o campo é nulo
        todo.setCreatedAt(null);

        Todo saved = repository.save(todo);

        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void shouldNotOverrideCreatedAt() {

        Instant fixed = Instant.parse("2020-01-01T00:00:00Z");

        Todo todo = new Todo(UUID.randomUUID(), "Test", "desc", false);
        todo.setCreatedAt(fixed);


        repository.save(todo);


        todo.setTitle("Change");
        Todo saved = repository.save(todo);


        assertEquals(fixed, saved.getCreatedAt());
    }

    @Test
    void shouldUpdateUpdatedAtOnSave() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "Desc", false);


        Todo firstSave = repository.save(todo);
        Instant updatedAt1 = firstSave.getUpdatedAt();


        firstSave.setTitle("Updated Title");


        try { Thread.sleep(10); } catch (InterruptedException ignored) {}

        Todo secondSave = repository.save(firstSave);
        Instant updatedAt2 = secondSave.getUpdatedAt();

        assertNotNull(updatedAt1);
        assertNotNull(updatedAt2);


        assertTrue(updatedAt2.isAfter(updatedAt1));
    }



    @Test
    void shouldReturnEmptyOptionalWhenTodoNotFound() {

        Optional<Todo> found = repository.findById(UUID.randomUUID());

        assertFalse(found.isPresent());
    }

    @Test
    void shouldReturnEmptyListWhenNoTodosExist() {

        List<Todo> todos = repository.findAll();

        assertTrue(todos.isEmpty());
        assertEquals(0, todos.size());
    }

    @Test
    void shouldDoNothingWhenDeletingNonExistentId() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "desc", false);
        repository.save(todo);


        UUID nonExistentId = UUID.randomUUID();
        repository.deleteById(nonExistentId);


        assertTrue(repository.findById(todo.getId()).isPresent());
        assertEquals(1, repository.findAll().size());
    }
}