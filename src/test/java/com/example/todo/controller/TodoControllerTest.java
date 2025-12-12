package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.usecase.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @MockBean private TodoService service;
    private Todo todo;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        todo = new Todo();
        todo.setId(id);
        todo.setTitle("Test");
        todo.setDescription("Desc");
        todo.setDone(false);
    }

    @Test
    void testList() throws Exception {
        when(service.list()).thenReturn(Arrays.asList(todo));
        mvc.perform(get("/api/todos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id.toString()));
        verify(service).list();
    }

    @Test
    void testListEmpty() throws Exception {
        when(service.list()).thenReturn(Arrays.asList());

        mvc.perform(get("/api/todos")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        verify(service).list();
    }

    @Test
    void testCreate() throws Exception {
        TodoRequest req = new TodoRequest();
        req.setTitle("New");
        req.setDescription("Desc");
        when(service.create(any(Todo.class))).thenReturn(todo);

        mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/todos/" + id)));
        verify(service).create(any(Todo.class));
    }

    @Test
    void testCreateInvalid() throws Exception {
        TodoRequest req = new TodoRequest();
        req.setTitle("");

        mvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        verify(service, never()).create(any());
    }

    @Test
    void testGet() throws Exception {
        when(service.find(id)).thenReturn(Optional.of(todo));
        mvc.perform(get("/api/todos/{id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id.toString()));
        verify(service).find(id);
    }

    @Test
    void testGetNotFound() throws Exception {
        when(service.find(id)).thenReturn(Optional.empty());
        mvc.perform(get("/api/todos/{id}", id)).andExpect(status().isNotFound());
        verify(service).find(id);
    }

    @Test
    void testUpdate() throws Exception {
        TodoRequest req = new TodoRequest();
        req.setTitle("Updated");
        when(service.update(eq(id), any(Todo.class))).thenReturn(todo);

        mvc.perform(put("/api/todos/{id}", id).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());
        verify(service).update(eq(id), any(Todo.class));
    }

    @Test
    void testUpdateInvalid() throws Exception {
        TodoRequest req = new TodoRequest();
        req.setTitle("");

        mvc.perform(put("/api/todos/{id}", id).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        verify(service, never()).update(any(UUID.class), any());
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(service).delete(id);

        mvc.perform(delete("/api/todos/{id}", id)).andExpect(status().isNoContent());
        verify(service).delete(id);
    }
}