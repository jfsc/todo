package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @Mock
    private TodoService service;

    private TodoController controller;

    @BeforeEach
    void setUp() {
        controller = new TodoController(service);
    }

    @Test
    void listShouldReturnMappedResponses() {
        Todo t1 = new Todo(UUID.randomUUID(), "t1", "d1", false);
        Todo t2 = new Todo(UUID.randomUUID(), "t2", "d2", true);
        Instant now = Instant.now();
        t1.setCreatedAt(now);
        t1.setUpdatedAt(now);
        t2.setCreatedAt(now);
        t2.setUpdatedAt(now);

        when(service.list()).thenReturn(List.of(t1, t2));

        List<TodoResponse> result = controller.list();

        assertEquals(2, result.size());
        TodoResponse r1 = result.get(0);
        assertEquals(t1.getId(), r1.getId());
        assertEquals("t1", r1.getTitle());
        assertEquals("d1", r1.getDescription());
        assertFalse(r1.isDone());
        TodoResponse r2 = result.get(1);
        assertEquals(t2.getId(), r2.getId());
        assertEquals("t2", r2.getTitle());
        assertEquals("d2", r2.getDescription());
        assertTrue(r2.isDone());
        verify(service).list();
    }

    @Test
    void createShouldMapRequestToDomainAndReturnCreatedResponse() {
        TodoRequest req = new TodoRequest();
        req.setTitle("New");
        req.setDescription("Desc");
        req.setDone(true);

        UUID id = UUID.randomUUID();
        Instant created = Instant.now();
        Instant updated = created;
        Todo saved = new Todo(id, "New", "Desc", true);
        saved.setCreatedAt(created);
        saved.setUpdatedAt(updated);

        when(service.create(any(Todo.class))).thenReturn(saved);

        ResponseEntity<TodoResponse> response = controller.create(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/api/todos/" + id), response.getHeaders().getLocation());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("New", response.getBody().getTitle());
        assertEquals("Desc", response.getBody().getDescription());
        assertTrue(response.getBody().isDone());

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
        verify(service).create(captor.capture());
        Todo passed = captor.getValue();
        assertEquals("New", passed.getTitle());
        assertEquals("Desc", passed.getDescription());
        assertTrue(passed.isDone());
    }

    @Test
    void getShouldReturnResponseWhenTodoExists() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Title", "Desc", false);
        Instant created = Instant.now();
        Instant updated = created;
        todo.setCreatedAt(created);
        todo.setUpdatedAt(updated);

        when(service.find(id)).thenReturn(Optional.of(todo));

        TodoResponse response = controller.get(id);

        assertEquals(id, response.getId());
        assertEquals("Title", response.getTitle());
        assertEquals("Desc", response.getDescription());
        assertFalse(response.isDone());
        assertEquals(created, response.getCreatedAt());
        assertEquals(updated, response.getUpdatedAt());
        verify(service).find(id);
    }

    @Test
    void getShouldThrowWhenTodoDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(service.find(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.get(id));
        assertEquals("Not found", ex.getMessage());
        verify(service).find(id);
    }

    @Test
    void updateShouldMapRequestAndReturnMappedResponse() {
        UUID id = UUID.randomUUID();

        TodoRequest req = new TodoRequest();
        req.setTitle("Updated");
        req.setDescription("New desc");
        req.setDone(true);

        Todo updatedDomain = new Todo(id, "Updated", "New desc", true);
        Instant created = Instant.now();
        Instant updated = created.plusSeconds(5);
        updatedDomain.setCreatedAt(created);
        updatedDomain.setUpdatedAt(updated);

        when(service.update(eq(id), any(Todo.class))).thenReturn(updatedDomain);

        TodoResponse response = controller.update(id, req);

        assertEquals(id, response.getId());
        assertEquals("Updated", response.getTitle());
        assertEquals("New desc", response.getDescription());
        assertTrue(response.isDone());
        assertEquals(created, response.getCreatedAt());
        assertEquals(updated, response.getUpdatedAt());

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
        verify(service).update(eq(id), captor.capture());
        Todo passed = captor.getValue();
        assertEquals("Updated", passed.getTitle());
        assertEquals("New desc", passed.getDescription());
        assertTrue(passed.isDone());
    }

    @Test
    void deleteShouldCallServiceDelete() {
        UUID id = UUID.randomUUID();

        controller.delete(id);

        verify(service).delete(id);
    }
}
