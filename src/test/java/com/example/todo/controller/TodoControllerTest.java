package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;

import com.example.todo.dto.TodoResponse;
import com.example.todo.repository.InMemoryTodoRepository;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TodoControllerTest {

    private InMemoryTodoRepository repository;
    private TodoService service;
    private TodoController controller;


    @BeforeEach
    void setup() {
        repository = new InMemoryTodoRepository();
        service = new TodoService(repository);
        controller = new TodoController(service);
    }

    @Test
    void list() {
        TodoRequest req = new TodoRequest();
        req.setTitle("test");
        req.setDescription("test");
        req.setDone(false);

        TodoRequest req2 = new TodoRequest();
        req2.setTitle("test2");
        req2.setDescription("test2");
        req2.setDone(true);

        ResponseEntity<TodoResponse> created = controller.create(req);
        ResponseEntity<TodoResponse> created2 = controller.create(req2);

        List<TodoResponse> resultsFound = controller.list();
        assertEquals(2, resultsFound.size());
    }

    @Test
    void create() {
        TodoRequest req = new TodoRequest();
        req.setTitle("test");
        req.setDescription("test");
        req.setDone(false);

        ResponseEntity<TodoResponse> created = controller.create(req);

        Optional<Todo> found = repository.findById(created.getBody().getId());
        assertTrue(found.isPresent());

    }

    @Test
    void get() {
        TodoRequest req = new TodoRequest();
        req.setTitle("test");
        req.setDescription("test");
        req.setDone(false);

        ResponseEntity<TodoResponse> created = controller.create(req);

        TodoResponse found = controller.get(created.getBody().getId());
        assertNotNull(found);
        assertEquals("test", found.getTitle());
    }

    @Test
    void update() {
        TodoRequest req = new TodoRequest();
        req.setTitle("test");
        req.setDescription("test");
        req.setDone(false);

        ResponseEntity<TodoResponse> created = controller.create(req);
        assertNotNull(created);
        assertNotNull(created.getBody());

      TodoRequest update = new TodoRequest();
      update.setTitle("test update");
        update.setDescription("test updated");
        update.setDone(true);

        TodoResponse updated = controller.update(created.getBody().getId(), update);
        assertEquals("test update", updated.getTitle());
        assertEquals("test updated", updated.getDescription());
        assertTrue(updated.isDone());


    }

    @Test
    void delete() {
        TodoRequest req = new TodoRequest();
        req.setTitle("test");
        req.setDescription("test");
        req.setDone(false);


    }
}