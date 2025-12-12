package com.example.todo.repository.Controller;

import com.example.todo.controller.TodoController;
import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.usecase.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService service;

    private static final String API_URL = "/api/todos";

    // --- (POST) Criar ---
    @Test
    void shouldCreateNewTodoAndReturn201() throws Exception {
        UUID id = UUID.randomUUID();
        TodoRequest request = new TodoRequest("New Task", "Description", false);
        Todo createdTodo = new Todo(id, "New Task", "Description", false);

        when(service.create(any(Todo.class))).thenReturn(createdTodo);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", API_URL + "/" + id))
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.title", is("New Task")));

        verify(service, times(1)).create(any(Todo.class));
    }

    // --- (GET by ID) Encontrar ---
    @Test
    void shouldReturnTodoAnd200WhenFound() throws Exception {
        UUID id = UUID.randomUUID();
        Todo foundTodo = new Todo(id, "Found Task", "Desc", true);

        when(service.find(id)).thenReturn(Optional.of(foundTodo));

        mockMvc.perform(get(API_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.done", is(true)));

        verify(service, times(1)).find(id);
    }

    @Test
    void shouldReturn404WhenTodoNotFoundOnGet() throws Exception {
        UUID id = UUID.randomUUID();

        when(service.find(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(API_URL + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(service, times(1)).find(id);
    }

    // --- (GET ALL) Listar ---
    @Test
    void shouldListAllTodosAndReturn200() throws Exception {
        Todo todo1 = new Todo(UUID.randomUUID(), "T1", "D1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "T2", "D2", true);
        List<Todo> todos = Arrays.asList(todo1, todo2);

        when(service.list()).thenReturn(todos);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("T1")))
                .andExpect(jsonPath("$[1].done", is(true)));

        verify(service, times(1)).list();
    }

    // --- (PUT) Atualizar ---
    @Test
    void shouldUpdateTodoAndReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        TodoRequest request = new TodoRequest("Updated Title", "Updated Desc", true);
        Todo updatedTodo = new Todo(id, "Updated Title", "Updated Desc", true);

        when(service.update(eq(id), any(Todo.class))).thenReturn(updatedTodo);

        mockMvc.perform(put(API_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.done", is(true)));

        verify(service, times(1)).update(eq(id), any(Todo.class));
    }

    // --- (DELETE) Excluir ---
    @Test
    void shouldDeleteTodoAndReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(service).delete(id);

        mockMvc.perform(delete(API_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(id);
    }

    @Test
    void shouldReturn404WhenTodoNotFoundOnDelete() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new RuntimeException("Not found")).when(service).delete(id);

        mockMvc.perform(delete(API_URL + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(service, times(1)).delete(id);
    }
}
