package com.example.todo.repository;

import com.example.todo.domain.Todo;
import com.example.todo.exception.GlobalExceptionHandler;
import com.example.todo.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.*;
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
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todo);

        List<Todo> lista = repository.findAll();
        assertFalse(lista.isEmpty());
    }

    @Test
    void shouldUpdateTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao teste 1", false);
        repository.save(todo);

        todo.setTitle("novo título");
        todo.setDescription("nova desc");
        todo.setDone(true);
        repository.save(todo);

        Todo updated = repository.findById(todo.getId()).orElseThrow();

        assertEquals("novo título", updated.getTitle());
        assertEquals("nova desc", updated.getDescription());
        assertTrue(updated.isDone());
    }

    @Test
    void shouldDeleteTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todo);
        repository.deleteById(todo.getId());
        assertTrue(repository.findById(todo.getId()).isEmpty());
    }

    @Test
    void saveShouldCreateIdWhenNull() {
        Todo t = new Todo();
        t.setId(null);
        Todo saved = repository.save(t);
        assertNotNull(saved.getId());
    }

    @Test
    void saveShouldNotOverwriteExistingId() {
        UUID id = UUID.randomUUID();
        Todo t = new Todo();
        t.setId(id);
        Todo saved = repository.save(t);
        assertEquals(id, saved.getId());
    }

    @Test
    void shouldCreateTodoUsingFirstConstructor() {
        Todo todo = new Todo("titulo", "descricao");

        assertNotNull(todo.getId());
        assertEquals("titulo", todo.getTitle());
        assertEquals("descricao", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldCreateTodoUsingSecondConstructor() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "titulo", true);

        assertEquals(id, todo.getId());
        assertEquals("titulo", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void getterShouldReturnUpdatedAt() {
        Todo todo = new Todo("Titulo", "Descricao");
        Instant updated = todo.getUpdatedAt();
        assertEquals(updated, todo.getUpdatedAt());
    }

    @Test
    void testHandleRuntime() {
        // Arrange
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        RuntimeException ex = new RuntimeException("Mensagem de erro");

        // Act
        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        // Assert: status
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Assert: body
        assertNotNull(response.getBody());
        Map<String, Object> body = response.getBody();

        assertEquals("about:blank", body.get("type"));
        assertEquals("Mensagem de erro", body.get("title"));
        assertEquals(404, body.get("status"));

        // Timestamp presente e não vazio
        assertTrue(body.containsKey("timestamp"));
        assertNotNull(body.get("timestamp"));
        assertFalse(body.get("timestamp").toString().isBlank());
    }

}