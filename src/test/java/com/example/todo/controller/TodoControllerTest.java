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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldListTodos() throws Exception {
        Todo todo = new Todo(UUID.randomUUID(), "List Test", "Teste de Listagem", false);

        Mockito.when(service.list()).thenReturn(List.of(todo));
        mvc.perform(get("/api/todos")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("List Test"));
    }

    @Test
    void shouldCreateTodo() throws Exception {
        TodoRequest todoRequest = new TodoRequest();

        todoRequest.setTitle("Create Test");
        todoRequest.setDescription("Teste de Criação");

        Todo todo = new Todo(UUID.randomUUID(), todoRequest.getTitle(), todoRequest.getDescription(), false);

        Mockito.when(service.create(any(Todo.class))).thenReturn(todo);
        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Create Test"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldReturnTodo() throws Exception {
        Todo todo = new Todo(UUID.randomUUID(), "Return Test", "Teste de Retorno", false);

        Mockito.when(service.find(todo.getId())).thenReturn(Optional.of(todo));

        mvc.perform(get("/api/todos/{id}", todo.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todo.getId().toString()));
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        TodoRequest todoRequest = new TodoRequest();

        todoRequest.setTitle("Update Test (2)");

        Todo updatedTodo = new Todo(UUID.randomUUID(), "Update Test (2)", "Teste de Update", false);
        Mockito.when(service.update(eq(updatedTodo.getId()), any(Todo.class))).thenReturn(updatedTodo);

        mvc.perform(put("/api/todos/{id}", updatedTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Update Test (2)"));
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/todos/{id}", id)).andExpect(status().isNoContent());
        verify(service).delete(id);
    }

}
