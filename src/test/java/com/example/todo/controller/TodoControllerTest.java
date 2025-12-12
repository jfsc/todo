package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService service;

    @Test
    void testList() throws Exception {
        when(service.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testCreate() throws Exception {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Titulo", "Desc", false);

        when(service.create(any())).thenReturn(todo);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Titulo\",\"description\":\"Desc\",\"done\":false}")
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/todos/" + id))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Titulo"));
    }

    @Test
    void testGet() throws Exception {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Teste", "Desc", false);

        when(service.find(id)).thenReturn(Optional.of(todo));

        mockMvc.perform(get("/api/todos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Teste"));
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        Todo updated = new Todo(id, "Novo", "Desc", true);

        when(service.update(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/api/todos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Novo\",\"description\":\"Desc\",\"done\":true}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Novo"));
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        // delete() do controller não retorna Optional nem boolean
        // então apenas garantimos que o método não explode
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/api/todos/" + id))
                .andExpect(status().isNoContent());
    }
}
