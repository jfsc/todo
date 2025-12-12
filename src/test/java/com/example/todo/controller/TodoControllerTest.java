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

/**
 * Tests for the {@link TodoController}.  These tests instantiate the
 * controller directly with a real service and repository.  By bypassing
 * the web layer we can focus on the controller's behaviour — i.e. that
 * it delegates to the service correctly, maps request bodies to domain
 * objects and translates domain objects to response DTOs.  Error
 * conditions are also exercised to verify that the controller throws
 * exceptions when appropriate.
 */
class TodoControllerTest {
    private TodoController controller;

    @BeforeEach
    void setUp() {
        controller = new TodoController(new TodoService(new InMemoryTodoRepository()));
    }

    @Test
    void listShouldReturnEmptyInitially() {
        List<TodoResponse> list = controller.list();
        assertNotNull(list);
        assertTrue(list.isEmpty(), "No todos should be returned for a fresh controller");
    }

    @Test
    void createShouldReturnCreatedResponseEntity() {
        TodoRequest req = new TodoRequest();
        req.setTitle("Build API");
        req.setDescription("Create endpoints");
        req.setDone(false);
        ResponseEntity<TodoResponse> response = controller.create(req);
        assertEquals(201, response.getStatusCodeValue());
        TodoResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Build API", body.getTitle());
        assertFalse(body.isDone());
        // Location header should include the generated id
        assertTrue(response.getHeaders().getLocation().toString().contains(body.getId().toString()));
    }

    @Test
    void getShouldReturnPersistedTodo() {
        TodoRequest req = new TodoRequest();
        req.setTitle("Read specification");
        req.setDescription("Understand requirements");
        req.setDone(false);
        UUID id = controller.create(req).getBody().getId();
        TodoResponse res = controller.get(id);
        assertEquals(id, res.getId());
        assertEquals("Read specification", res.getTitle());
    }

    @Test
    void getShouldThrowWhenTodoMissing() {
        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> controller.get(id));
    }

    @Test
    void updateShouldModifyTodo() {
        TodoRequest create = new TodoRequest();
        create.setTitle("Initial");
        create.setDescription("Desc");
        create.setDone(false);
        UUID id = controller.create(create).getBody().getId();

        TodoRequest update = new TodoRequest();
        update.setTitle("Updated title");
        update.setDescription("Updated desc");
        update.setDone(true);
        TodoResponse updated = controller.update(id, update);
        assertEquals("Updated title", updated.getTitle());
        assertEquals("Updated desc", updated.getDescription());
        assertTrue(updated.isDone());
    }

    @Test
    void updateShouldThrowWhenMissing() {
        TodoRequest update = new TodoRequest();
        update.setTitle("Doesn't matter");
        update.setDescription("None");
        update.setDone(true);
        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> controller.update(id, update));
    }

    @Test
    void deleteShouldRemoveTodo() {
        TodoRequest req = new TodoRequest();
        req.setTitle("Remove me");
        req.setDescription("To be removed");
        req.setDone(false);
        UUID id = controller.create(req).getBody().getId();
        ResponseEntity<Void> resp = controller.delete(id);
        assertEquals(204, resp.getStatusCodeValue());
        // subsequent retrieval should throw since it's gone
        assertThrows(RuntimeException.class, () -> controller.get(id));
    }

    @Test
    void deleteShouldThrowWhenMissing() {
        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> controller.delete(id));
    }
}