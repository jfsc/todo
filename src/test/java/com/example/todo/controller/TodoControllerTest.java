package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
    void testListLambdaMapping() {
        Todo todo = new Todo(UUID.randomUUID(), "Title", "Desc", false);
        when(service.list()).thenReturn(List.of(todo));

        List<TodoResponse> responses = controller.list();

        assertEquals(1, responses.size());
        assertEquals(todo.getId(), responses.get(0).getId());
        verify(service, times(1)).list();
    }
}
