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

        Optional<Todo> encontrado = repository.findById(todo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Test", encontrado.get().getTitle());
    }

    @Test
    void shouldCreateTodoNull() {
        Todo todoNull = new Todo(null, null, null, false);
        repository.save(todoNull);
        Optional<Todo> found = repository.findById(todoNull.getId());
        assertTrue(found.isPresent());

    }

    @Test
    void shouldListTodos() {
        Todo todo1 = new Todo(UUID.randomUUID(), "A", "descricao A", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "B", "descricao B", true);

        repository.save(todo1);
        repository.save(todo2);

        List<Todo> todos = repository.findAll();

        assertEquals(2, todos.size());
        assertTrue(todos.contains(todo1));
        assertTrue(todos.contains(todo2));
    }

    @Test
    void shouldUpdateTodo() {
        Todo todo1 = new Todo(UUID.randomUUID(),"Kamila task", "Kamila", false);
        repository.save(todo1);

        todo1.setTitle("Kamila test");
        todo1.setDescription("Updated description");
        todo1.setDone(true);

        repository.save(todo1);

        Optional<Todo> updated = repository.findById(todo1.getId());

        assertTrue(updated.isPresent());
        assertEquals("Kamila test", updated.get().getTitle());
        assertEquals("Updated description", updated.get().getDescription());
        assertTrue(updated.get().isDone());
    }

    @Test
    void shouldDeleteTodo() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Deletar", "deletar", false);

        repository.save(todo);
        repository.deleteById(id);

        Optional<Todo> encontrado = repository.findById(id);

        assertTrue(encontrado.isEmpty(), "O todo deveria ter sido deletado");
    }

    @Test
    void shouldFindTodoById() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Achar pelo id", "teste", false);

        repository.save(todo);

        Optional<Todo> encontrado = repository.findById(id);

        assertTrue(encontrado.isPresent());
        assertEquals(id, encontrado.get().getId());
    }

    @Test
    void shouldNotFindTodoById() {
        UUID id = UUID.randomUUID();

        Optional<Todo> encontrado = repository.findById(id);

        assertTrue(encontrado.isEmpty());
    }
}