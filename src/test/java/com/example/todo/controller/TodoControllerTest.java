package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;

import com.example.todo.dto.TodoResponse;
import com.example.todo.exception.GlobalExceptionHandler;
import com.example.todo.repository.InMemoryTodoRepository;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;
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
    void shouldList() {
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
    void shouldCreate() {
        TodoRequest req = new TodoRequest();
        req.setTitle("test");
        req.setDescription("test");
        req.setDone(false);

        ResponseEntity<TodoResponse> created = controller.create(req);

        Optional<Todo> found = repository.findById(created.getBody().getId());
        assertTrue(found.isPresent());

    }

    @Test
    void shouldGetById() {
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
    void shouldUpdate() {
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
    void update_shouldReturn404AndFailWithInvalidId() {
        TodoRequest todo = new TodoRequest();
        todo.setTitle("Learn JUnit5");
        todo.setDescription("You should learn JUnit");
        todo.setDone(false);

        ResponseEntity<TodoResponse> created = controller.create(todo);
        assertNotNull(created);
        assertNotNull(created.getBody());

        TodoRequest update = new TodoRequest();
        update.setTitle("Fish swim");
        update.setDescription("Fish sleep with eyes open");
        update.setDone(true);

        UUID id = UUID.nameUUIDFromBytes("hi fish".getBytes());


        assertThrows(RuntimeException.class, () -> {
            TodoResponse updatedTodo = controller.update(id, update);
        });
    }


    @Test
    void delete_shouldNotFoundEntity() {
        TodoRequest req = new TodoRequest();
        req.setTitle("test");
        req.setDescription("test");
        req.setDone(false);

        ResponseEntity<TodoResponse> created = controller.create(req);
        assertNotNull(created);
        assertNotNull(created.getBody());

        controller.delete(created.getBody().getId());
        assertThrows(RuntimeException.class, () -> {
            controller.get(created.getBody().getId());
        });
    }
}