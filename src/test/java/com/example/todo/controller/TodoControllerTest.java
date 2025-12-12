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
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    private TodoRequest todoRequest;
    private Todo todoDomain;
//    private TodoResponse todoResponse;

    @BeforeEach
    void setUp() {
        todoRequest = new TodoRequest();
        todoRequest.setTitle("Test Todo");
        todoRequest.setDescription("Description for Test Todo");
        todoRequest.setDone(false);

        todoDomain = new Todo();
        todoDomain.setId(UUID.randomUUID());
        todoDomain.setTitle(todoRequest.getTitle());
        todoDomain.setDescription(todoRequest.getDescription());
        todoDomain.setDone(todoRequest.isDone());
        todoDomain.setCreatedAt(Instant.now());
        todoDomain.setUpdatedAt(Instant.now());
    }

    @Test
    void createTodo_shouldReturnCreatedTodo_whenValidRequest() throws Exception {
        when(todoService.create(any(Todo.class))).thenReturn(todoDomain);

        ResultActions resultActions = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoRequest)));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(header().string("Location", "/api/todos/" + todoDomain.getId()));
        resultActions.andExpect(jsonPath("$.id").value(todoDomain.getId().toString()));
        resultActions.andExpect(jsonPath("$.title").value(todoRequest.getTitle()));
        resultActions.andExpect(jsonPath("$.description").value(todoRequest.getDescription()));
        resultActions.andExpect(jsonPath("$.done").value(todoRequest.isDone()));
    }

    @Test
    void updateTodo_shouldUpdatedTodo_whenReceiveIdAndBodyRequest() throws Exception {
        // Arrange
        UUID todoId = todoDomain.getId();
        TodoRequest updateRequest = new TodoRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");
        updateRequest.setDone(true);

        Todo updatedTodo = new Todo();
        updatedTodo.setId(todoId);
        updatedTodo.setTitle(updateRequest.getTitle());
        updatedTodo.setDescription(updateRequest.getDescription());
        updatedTodo.setDone(updateRequest.isDone());
        updatedTodo.setCreatedAt(todoDomain.getCreatedAt()); // CreatedAt should remain the same
        updatedTodo.setUpdatedAt(Instant.now());             // UpdatedAt should change

        when(todoService.update(eq(todoId), any(Todo.class))).thenReturn(updatedTodo);

        // Act
        ResultActions resultActions = mockMvc.perform(put("/api/todos/{id}", todoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        // Assert
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(todoId.toString()));
        resultActions.andExpect(jsonPath("$.title").value(updateRequest.getTitle()));
        resultActions.andExpect(jsonPath("$.description").value(updateRequest.getDescription()));
        resultActions.andExpect(jsonPath("$.done").value(updateRequest.isDone()));

        // Verify that the service method was called with the correct ID
        verify(todoService).update(eq(todoId), any(Todo.class));
    }

    @Test
    void getTodoById_shouldReturnTodo_whenTodoExists() throws Exception {
        // Arrange
        UUID todoId = todoDomain.getId();
        when(todoService.find(todoId)).thenReturn(Optional.of(todoDomain));

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/todos/{id}", todoId));

        // Assert
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(todoId.toString()));
        resultActions.andExpect(jsonPath("$.title").value(todoDomain.getTitle()));
    }

    @Test
    void listTodos_shouldReturnTodoList() throws Exception {
        // Arrange
        List<Todo> todos = Collections.singletonList(todoDomain);
        when(todoService.list()).thenReturn(todos);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/todos"));

        // Assert
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].id", is(todoDomain.getId().toString())));
    }

    @Test
    void deleteTodo_shouldReturnNoContent_whenTodoExists() throws Exception {
        // Arrange
        UUID todoId = todoDomain.getId();
        doNothing().when(todoService).delete(todoId);

        // Act
        ResultActions resultActions = mockMvc.perform(delete("/api/todos/{id}", todoId));

        // Assert
        resultActions.andExpect(status().isNoContent());
        verify(todoService).delete(todoId);
    }
}
