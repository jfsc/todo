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
    void shouldGetTodoById() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title", "Desc", false);

        when(service.find(id)).thenReturn(Optional.of(todo));

        TodoResponse response = controller.get(id);

        assertNotNull(response);
        assertEquals(todo.getId(), response.getId());
        verify(service, times(1)).find(id);
    }

    @Test
    void shouldUpdateTodo() {
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
    void shouldDeleteTodo() {
        UUID id = UUID.randomUUID();

        doNothing().when(service).delete(id);

        controller.delete(id);
        verify(service, times(1)).delete(id);
    }

    @Test
    void shouldListTodos() {
        Todo todo = new Todo(UUID.randomUUID(), "Title", "Desc", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Title 2", "Desc", true);

        when(service.list()).thenReturn(List.of(todo, todo2));

        List<TodoResponse> responses = controller.list();

        assertEquals(2, responses.size());
        assertEquals(todo.getId(), responses.get(0).getId());
        assertEquals(todo2.getId(), responses.get(1).getId());
        verify(service, times(1)).list();
    }
}