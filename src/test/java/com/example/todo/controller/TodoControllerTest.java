package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.usecase.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TodoService service;

    // GET /api/todos
    @Test
    void shouldListTodos() throws Exception {
        List<Todo> todos = List.of(
                new Todo(UUID.randomUUID(), "Task 1", "Desc 1", false),
                new Todo(UUID.randomUUID(), "Task 2", "Desc 2", true)
        );

        Mockito.when(service.list()).thenReturn(todos);

        mvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    // POST /api/todos
    @Test
    void shouldCreateTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo created = new Todo(id, "New Task", "Some description", false);

        Mockito.when(service.create(any())).thenReturn(created);

        TodoRequest request = new TodoRequest();
        request.setTitle("New Task");
        request.setDescription("Some description");

        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/todos/" + id))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    // GET /api/todos/{id}
    @Test
    void shouldGetTodoById() throws Exception {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Task", "Some Desc", true);

        Mockito.when(service.find(id)).thenReturn(Optional.of(todo));

        mvc.perform(get("/api/todos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Task"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void shouldReturn500WhenTodoNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(service.find(id)).thenReturn(Optional.empty());

        mvc.perform(get("/api/todos/" + id))
                .andExpect(status().isNotFound());
    }

    // PUT /api/todos/{id}
    @Test
    void shouldUpdateTodo() throws Exception {
        UUID id = UUID.randomUUID();

        Todo updated = new Todo(id, "Updated", "Desc updated", true);
        Mockito.when(service.update(eq(id), any())).thenReturn(updated);

        TodoRequest req = new TodoRequest();
        req.setTitle("Updated");
        req.setDescription("Desc updated");

        mvc.perform(put("/api/todos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.done").value(true));
    }

    // DELETE /api/todos/{id}
    @Test
    void shouldDeleteTodo() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/todos/" + id))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(id);
    }
}