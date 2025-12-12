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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldListTodos() throws Exception {
        Todo t = new Todo(UUID.randomUUID(), "Test", "Desc", false);
        Mockito.when(service.list()).thenReturn(List.of(t));

        mvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test"));
    }

    @Test
    void shouldCreateTodo() throws Exception {
        TodoRequest req = new TodoRequest();
        req.setTitle("New Todo");
        req.setDescription("Desc");

        Todo created = new Todo(UUID.randomUUID(), "New Todo", "Desc", false);

        Mockito.when(service.create(any(Todo.class))).thenReturn(created);

        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("New Todo"));
    }

    @Test
    void shouldReturnTodoById() throws Exception {
        UUID id = UUID.randomUUID();
        Todo t = new Todo(id, "Test", "Desc", false);

        Mockito.when(service.find(id)).thenReturn(Optional.of(t));

        mvc.perform(get("/api/todos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void shouldReturn404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(service.find(id)).thenReturn(Optional.empty());

        mvc.perform(get("/api/todos/" + id))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        UUID id = UUID.randomUUID();

        TodoRequest req = new TodoRequest();
        req.setTitle("Updated Title");
        req.setDescription("Updated Desc");
        req.setDone(true);

        Todo updated = new Todo(id, "Updated Title", "Updated Desc", true);

        Mockito.when(service.update(eq(id), any(Todo.class))).thenReturn(updated);

        mvc.perform(put("/api/todos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void shouldReturnNoContentOnDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/todos/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldValidateTitleOnCreate() throws Exception {
        TodoRequest req = new TodoRequest();
        req.setTitle("");

        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
