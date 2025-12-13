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

    private TodoService todoService;

    @BeforeEach
    void setUpService() {
        todoService = new TodoService(new InMemoryTodoRepository());
    }

    @Test
    void shouldCreateTodoAndSetTimestamps() {
        Todo todo = new Todo("Service layer test", "Validating create operation");

        Todo createdTodo = todoService.create(todo);

        assertNotNull(createdTodo.getId());
        assertNotNull(createdTodo.getCreatedAt(), "Created timestamp must be set");
        assertNotNull(createdTodo.getUpdatedAt());
    }

    @Test
    void shouldReturnTodoWhenFoundById() {
        Todo todo = new Todo("Find todo", "Testing find by id");
        Todo savedTodo = todoService.create(todo);

        Optional<Todo> result = todoService.find(savedTodo.getId());

        assertTrue(result.isPresent());
        assertEquals("Find todo", result.get().getTitle());
    }

    @Test
    void shouldReturnAllTodos() {
        Todo firstTodo = new Todo("First task", "First description");
        Todo secondTodo = new Todo("Second task", "Second description");

        todoService.create(firstTodo);
        todoService.create(secondTodo);

        List<Todo> todos = todoService.list();

        assertEquals(2, todos.size());
    }

    @Test
    void shouldUpdateExistingTodo() {
        Todo originalTodo = new Todo("Update task", "Old description");
        Todo storedTodo = todoService.create(originalTodo);

        Todo updatedData = new Todo("Updated task", "New description");
        updatedData.setDone(true);

        Todo updatedTodo = todoService.update(storedTodo.getId(), updatedData);

        assertEquals("Updated task", updatedTodo.getTitle());
        assertEquals("New description", updatedTodo.getDescription());
        assertTrue(updatedTodo.isDone());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTodo() {
        Todo updatedData = new Todo("Irrelevant title", "Irrelevant description");
        UUID nonExistingId = UUID.randomUUID();

        assertThrows(RuntimeException.class,
                () -> todoService.update(nonExistingId, updatedData));
    }

    @Test
    void shouldDeleteExistingTodo() {
        Todo todo = new Todo("Delete task", "Testing delete operation");
        Todo savedTodo = todoService.create(todo);

        todoService.delete(savedTodo.getId());

        assertTrue(todoService.find(savedTodo.getId()).isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTodo() {
        UUID nonExistingId = UUID.randomUUID();

        assertThrows(RuntimeException.class,
                () -> todoService.delete(nonExistingId));
    }
}
