package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    private TodoService service;
    private TodoController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(TodoService.class);
        controller = new TodoController(service);
    }

    @Test
    void testGet() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title", "Desc", false);
        when(service.find(id)).thenReturn(Optional.of(todo));

        TodoResponse response = controller.get(id);

        assertNotNull(response);
        assertEquals(todo.getId(), response.getId());
        assertEquals(todo.getTitle(), response.getTitle());
        assertEquals(todo.getDescription(), response.getDescription());
        assertEquals(todo.isDone(), response.isDone());

        verify(service, times(1)).find(id);
    }

    @Test
    void testGetNotFound() {
        UUID id = UUID.randomUUID();
        when(service.find(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.get(id));
        assertEquals("Not found", ex.getMessage());

        verify(service, times(1)).find(id);
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        TodoRequest request = new TodoRequest();
        request.setTitle("New Title");
        request.setDescription("New Desc");
        request.setDone(true);

        Todo updated = new Todo(id, request.getTitle(), request.getDescription(), request.isDone());
        when(service.update(eq(id), any())).thenReturn(updated);

        TodoResponse response = controller.update(id, request);

        assertNotNull(response);
        assertEquals(updated.getId(), response.getId());
        assertEquals(updated.getTitle(), response.getTitle());
        assertEquals(updated.getDescription(), response.getDescription());
        assertEquals(updated.isDone(), response.isDone());

        verify(service, times(1)).update(eq(id), any());
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        doNothing().when(service).delete(id);

        controller.delete(id);

        verify(service, times(1)).delete(id);
    }

    @Test
    void testList() {
        Todo todo = new Todo(UUID.randomUUID(), "Title", "Desc", false);
        when(service.list()).thenReturn(List.of(todo));

        List<TodoResponse> responses = controller.list();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(todo.getId(), responses.get(0).getId());
        assertEquals(todo.getTitle(), responses.get(0).getTitle());
        assertEquals(todo.getDescription(), responses.get(0).getDescription());

        verify(service, times(1)).list();
    }

    @Test
    void testCreate() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Title");
        request.setDescription("Desc");
        request.setDone(false);

        Todo created = new Todo(UUID.randomUUID(), request.getTitle(), request.getDescription(), request.isDone());
        when(service.create(any(Todo.class))).thenReturn(created);

        ResponseEntity<TodoResponse> responseEntity = controller.create(request);

        assertNotNull(responseEntity);
        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals(created.getId(), responseEntity.getBody().getId());
        assertEquals(created.getTitle(), responseEntity.getBody().getTitle());
        assertEquals(created.getDescription(), responseEntity.getBody().getDescription());
        assertEquals(created.isDone(), responseEntity.getBody().isDone());

        verify(service, times(1)).create(any(Todo.class));
    }
}
