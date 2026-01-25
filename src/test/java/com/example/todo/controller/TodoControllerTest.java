package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.usecase.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFetchAllTodos() throws Exception {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Test Todo", "Description", false);
        todo.setCreatedAt(Instant.now());
        todo.setUpdatedAt(Instant.now());

        given(service.list()).willReturn(List.of(todo));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(todo.getTitle())));
    }

    @Test
    void shouldCreateNewTodo() throws Exception {
        Todo todo = new Todo(UUID.randomUUID(), "New Todo", "Description", false);
        given(service.create(any(Todo.class))).willReturn(todo);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Todo")));
    }

    @Test
    void shouldGetTodoById() throws Exception {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Test Todo", "Description", false);
        given(service.find(id)).willReturn(Optional.of(todo));

        mockMvc.perform(get("/api/todos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())));
    }

    @Test
    void shouldReturnNotFoundForInvalidTodoId() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.find(id)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/todos/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Updated Todo", "Updated Desc", true);
        given(service.update(any(UUID.class), any(Todo.class))).willReturn(todo);

        mockMvc.perform(put("/api/todos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Todo")));
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        UUID id = UUID.randomUUID();
        // BDDMockito.doNothing().when(service).delete(id);
        
        mockMvc.perform(delete("/api/todos/{id}", id))
                .andExpect(status().isNoContent());
    }
}
