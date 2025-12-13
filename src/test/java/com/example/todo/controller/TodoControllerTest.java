package com.example.todo.controller;

import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.repository.InMemoryTodoRepository;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoControllerTest {

    private TodoController todoController;

    @BeforeEach
    void setUpController() {
        todoController = new TodoController(
                new TodoService(new InMemoryTodoRepository())
        );
    }

    @Test
    void shouldCreateTodoAndReturnCreatedResponse() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Build REST API");
        request.setDescription("Implement all endpoints");
        request.setDone(false);

        ResponseEntity<TodoResponse> response =
                todoController.create(request);

        assertEquals(201, response.getStatusCodeValue());

        TodoResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Build REST API", body.getTitle());
        assertFalse(body.isDone());

        assertTrue(
                response.getHeaders()
                        .getLocation()
                        .toString()
                        .contains(body.getId().toString())
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoTodosExist() {
        List<TodoResponse> responses = todoController.list();

        assertNotNull(responses);
        assertTrue(responses.isEmpty(),
                "A newly created controller should return an empty list");
    }

    @Test
    void shouldThrowExceptionWhenTodoDoesNotExist() {
        UUID nonExistingId = UUID.randomUUID();

        assertThrows(RuntimeException.class,
                () -> todoController.get(nonExistingId));
    }

    @Test
    void shouldUpdateExistingTodo() {
        TodoRequest createRequest = new TodoRequest();
        createRequest.setTitle("Initial task");
        createRequest.setDescription("Initial description");
        createRequest.setDone(false);

        UUID todoId = todoController.create(createRequest)
                .getBody()
                .getId();

        TodoRequest updateRequest = new TodoRequest();
        updateRequest.setTitle("Updated task");
        updateRequest.setDescription("Updated description");
        updateRequest.setDone(true);

        TodoResponse updatedResponse =
                todoController.update(todoId, updateRequest);

        assertEquals("Updated task", updatedResponse.getTitle());
        assertEquals("Updated description", updatedResponse.getDescription());
        assertTrue(updatedResponse.isDone());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTodo() {
        UUID nonExistingId = UUID.randomUUID();

        assertThrows(RuntimeException.class,
                () -> todoController.delete(nonExistingId));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTodo() {
        TodoRequest updateRequest = new TodoRequest();
        updateRequest.setTitle("Irrelevant title");
        updateRequest.setDescription("Irrelevant description");
        updateRequest.setDone(true);

        UUID nonExistingId = UUID.randomUUID();

        assertThrows(RuntimeException.class,
                () -> todoController.update(nonExistingId, updateRequest));
    }

    @Test
    void shouldDeleteTodoSuccessfully() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Delete task");
        request.setDescription("This task should be removed");
        request.setDone(false);

        UUID todoId = todoController.create(request)
                .getBody()
                .getId();

        ResponseEntity<Void> response =
                todoController.delete(todoId);

        assertEquals(204, response.getStatusCodeValue());

        assertThrows(RuntimeException.class,
                () -> todoController.get(todoId));
    }

    @Test
    void shouldReturnTodoWhenIdExists() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Read documentation");
        request.setDescription("Understand system requirements");
        request.setDone(false);

        UUID todoId = todoController.create(request)
                .getBody()
                .getId();

        TodoResponse response = todoController.get(todoId);

        assertEquals(todoId, response.getId());
        assertEquals("Read documentation", response.getTitle());
    }
}
