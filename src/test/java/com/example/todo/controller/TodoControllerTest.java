package com.example.todo.controller;

import com.example.todo.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.todo.dto.TodoRequest;
import com.example.todo.repository.InMemoryTodoRepository;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerTest {

    private MockMvc mvc;
    private InMemoryTodoRepository repo;
    private TodoService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        repo = new InMemoryTodoRepository();
        service = new TodoService(repo);
        mvc = standaloneSetup(new TodoController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCreate() throws Exception {
        TodoRequest req = new TodoRequest();
        req.setTitle("abc");
        req.setDescription("x");

        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void testList() throws Exception {
        repo.save(new com.example.todo.domain.Todo("a", "b"));

        mvc.perform(get("/api/todos"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFound() throws Exception {
        var saved = repo.save(new com.example.todo.domain.Todo("z", "y"));

        mvc.perform(get("/api/todos/" + saved.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNotFound() throws Exception {
        mvc.perform(get("/api/todos/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate() throws Exception {
        var saved = repo.save(new com.example.todo.domain.Todo("a", "b"));

        TodoRequest req = new TodoRequest();
        req.setTitle("updated");
        req.setDescription("new");

        mvc.perform(put("/api/todos/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated"));
    }

    @Test
    void testDelete() throws Exception {
        var saved = repo.save(new com.example.todo.domain.Todo("a", "b"));

        mvc.perform(delete("/api/todos/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
