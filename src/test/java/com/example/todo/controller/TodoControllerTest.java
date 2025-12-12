package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.usecase.TodoService;
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
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService service;

    // -------------------------
    // GET /api/todos
    // -------------------------
    @Test
    void shouldListTodos() throws Exception {
        Todo t = new Todo(UUID.randomUUID(), "Teste", false);
        Mockito.when(service.list()).thenReturn(List.of(t));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Teste"));
    }

    // -------------------------
    // POST /api/todos
    // -------------------------
    @Test
    void shouldCreateTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo created = new Todo(id, "Criar", false);

        Mockito.when(service.create(any(Todo.class))).thenReturn(created);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    { "title": "Criar", "description": "desc", "done": false }
                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/todos/" + id))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Criar"));
    }

    // -------------------------
    // GET /api/todos/{id}
    // -------------------------
    @Test
    void shouldGetTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo t = new Todo(id, "Buscar", false);

        Mockito.when(service.find(id)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/api/todos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Buscar"));
    }

    @Test
    void shouldReturn404WhenTodoNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(service.find(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/todos/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not found"));
    }

    // -------------------------
    // PUT /api/todos/{id}
    // -------------------------
    @Test
    void shouldUpdateTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo updated = new Todo(id, "Atualizado", true);

        Mockito.when(service.update(eq(id), any(Todo.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/todos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    { "title": "Atualizado", "description": "", "done": true }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Atualizado"))
                .andExpect(jsonPath("$.done").value(true));
    }

    // -------------------------
    // DELETE /api/todos/{id}
    // -------------------------
    @Test
    void shouldDeleteTodo() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/todos/" + id))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(id);
    }

    // -------------------------
    // POST com título vazio: validação
    // -------------------------
    @Test
    void shouldFailValidationOnCreate() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    { "title": "", "description": "desc", "done": false }
                """))
                .andExpect(status().isBadRequest());
    }
}
