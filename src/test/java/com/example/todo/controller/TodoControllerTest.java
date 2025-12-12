package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    private Todo sampleTodo;
    private UUID sampleId;
    private TodoRequest todoRequest;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleTodo = new Todo(sampleId, "Test Todo", "Test Description", false);
        
        todoRequest = new TodoRequest();
        todoRequest.setTitle("Test Todo");
        todoRequest.setDescription("Test Description");
        todoRequest.setDone(false);
    }

    @Test
    void shouldListAllTodos() throws Exception {
        Todo todo1 = new Todo(UUID.randomUUID(), "Todo 1", "Description 1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Todo 2", "Description 2", true);
        List<Todo> todos = Arrays.asList(todo1, todo2);

        when(todoService.list()).thenReturn(todos);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Todo 1"))
                .andExpect(jsonPath("$[1].title").value("Todo 2"));

        verify(todoService, times(1)).list();
    }

    @Test
    void shouldCreateTodo() throws Exception {
        when(todoService.create(any(Todo.class))).thenReturn(sampleTodo);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(sampleId.toString()))
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.done").value(false));

        verify(todoService, times(1)).create(any(Todo.class));
    }

    // @Test
    // void shouldNotCreateTodoWithBlankTitle() throws Exception {
    //     todoRequest.setTitle("");

    //     mockMvc.perform(post("/api/todos")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(objectMapper.writeValueAsString(todoRequest)))
    //             .andExpect(status().isBadRequest());

    //     verify(todoService, never()).create(any(Todo.class));
    // }

    // @Test
    // void shouldNotCreateTodoWithNullTitle() throws Exception {
    //     todoRequest.setTitle(null);

    //     mockMvc.perform(post("/api/todos")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(objectMapper.writeValueAsString(todoRequest)))
    //             .andExpect(status().isBadRequest());

    //     verify(todoService, never()).create(any(Todo.class));
    // }

    @Test
    void shouldGetTodoById() throws Exception {
        when(todoService.find(sampleId)).thenReturn(Optional.of(sampleTodo));

        mockMvc.perform(get("/api/todos/{id}", sampleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleId.toString()))
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(todoService, times(1)).find(sampleId);
    }

    // @Test
    // void shouldThrowExceptionWhenTodoNotFound() throws Exception {
    //     UUID nonExistentId = UUID.randomUUID();
    //     when(todoService.find(nonExistentId)).thenReturn(Optional.empty());

    //     mockMvc.perform(get("/api/todos/{id}", nonExistentId))
    //             .andExpect(status().isInternalServerError());

    //     verify(todoService, times(1)).find(nonExistentId);
    // }

    @Test
    void shouldUpdateTodo() throws Exception {
        Todo updatedTodo = new Todo(sampleId, "Updated Title", "Updated Description", true);
        when(todoService.update(eq(sampleId), any(Todo.class))).thenReturn(updatedTodo);

        TodoRequest updateRequest = new TodoRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");
        updateRequest.setDone(true);

        mockMvc.perform(put("/api/todos/{id}", sampleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.done").value(true));

        verify(todoService, times(1)).update(eq(sampleId), any(Todo.class));
    }

    // @Test
    // void shouldNotUpdateTodoWithBlankTitle() throws Exception {
    //     todoRequest.setTitle("");

    //     mockMvc.perform(put("/api/todos/{id}", sampleId)
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(objectMapper.writeValueAsString(todoRequest)))
    //             .andExpect(status().isBadRequest());

    //     verify(todoService, never()).update(any(), any());
    // }

    @Test
    void shouldDeleteTodo() throws Exception {
        doNothing().when(todoService).delete(sampleId);

        mockMvc.perform(delete("/api/todos/{id}", sampleId))
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).delete(sampleId);
    }
}