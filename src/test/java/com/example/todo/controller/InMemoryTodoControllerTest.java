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
class InMemoryTodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService service;

    @Test
    void shouldListTodos() throws Exception {
        Todo t = new Todo(UUID.randomUUID(), "Title", "Desc", false);

        Mockito.when(service.list()).thenReturn(List.of(t));

        mvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void shouldCreateTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo created = new Todo(id, "New task", "created desc", false);

        Mockito.when(service.create(any())).thenReturn(created);

        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "New task",
                                  "description": "created desc",
                                  "done": false
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/todos/" + id))
                .andExpect(jsonPath("$.title").value("New task"));
    }

    @Test
    void shouldGetTodoById() throws Exception {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Task", "Desc", false);

        Mockito.when(service.find(id)).thenReturn(Optional.of(todo));

        mvc.perform(get("/api/todos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task"));
    }

    @Test
    void shouldReturn404WhenTodoNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(service.find(id)).thenReturn(Optional.empty());

        mvc.perform(get("/api/todos/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo updated = new Todo(id, "Updated", "Updated desc", true);

        Mockito.when(service.update(eq(id), any())).thenReturn(updated);

        mvc.perform(put("/api/todos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated",
                                  "description": "Updated desc",
                                  "done": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/todos/" + id))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(id);
    }
}
