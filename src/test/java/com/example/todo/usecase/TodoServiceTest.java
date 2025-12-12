package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private InMemoryTodoRepository repository;

    @InjectMocks
    private TodoService service;

    private Todo sampleTodo;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleTodo = new Todo(sampleId, "Test Todo", "Test Description", false);
    }

    @Test
    void shouldCreateTodo() {
        when(repository.save(any(Todo.class))).thenReturn(sampleTodo);

        Todo result = service.create(sampleTodo);

        assertNotNull(result);
        assertEquals(sampleTodo.getTitle(), result.getTitle());
        verify(repository, times(1)).save(sampleTodo);
    }

    @Test
    void shouldFindTodoById() {
        when(repository.findById(sampleId)).thenReturn(Optional.of(sampleTodo));

        Optional<Todo> result = service.find(sampleId);

        assertTrue(result.isPresent());
        assertEquals(sampleTodo.getId(), result.get().getId());
        verify(repository, times(1)).findById(sampleId);
    }

     @Test
     void shouldReturnEmptyWhenTodoNotFound() {
         when(repository.findById(sampleId)).thenReturn(Optional.empty());

         Optional<Todo> result = service.find(sampleId);

         assertFalse(result.isPresent());
         verify(repository, times(1)).findById(sampleId);
     }

    @Test
    void shouldListAllTodos() {
        Todo todo1 = new Todo(UUID.randomUUID(), "Todo 1", "Description 1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "Todo 2", "Description 2", true);
        List<Todo> todos = Arrays.asList(todo1, todo2);

        when(repository.findAll()).thenReturn(todos);

        List<Todo> result = service.list();

        assertEquals(2, result.size());
        assertTrue(result.contains(todo1));
        assertTrue(result.contains(todo2));
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldUpdateTodo() {
        Todo updatedTodo = new Todo(sampleId, "Updated Title", "Updated Description", true);
        
        when(repository.findById(sampleId)).thenReturn(Optional.of(sampleTodo));
        when(repository.save(any(Todo.class))).thenReturn(sampleTodo);

        Todo result = service.update(sampleId, updatedTodo);

        assertNotNull(result);
        assertEquals("Updated Title", sampleTodo.getTitle());
        assertEquals("Updated Description", sampleTodo.getDescription());
        assertTrue(sampleTodo.isDone());
        verify(repository, times(1)).findById(sampleId);
        verify(repository, times(1)).save(sampleTodo);
    }

     @Test
     void shouldThrowExceptionWhenUpdatingNonExistentTodo() {
         UUID nonExistentId = UUID.randomUUID();
         Todo updateData = new Todo(nonExistentId, "New Title", "New Description", false);

         when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
             service.update(nonExistentId, updateData);
         });

         assertEquals("Not found", exception.getMessage());
         verify(repository, times(1)).findById(nonExistentId);
         verify(repository, never()).save(any());
     }

    @Test
    void shouldDeleteTodo() {
        doNothing().when(repository).deleteById(sampleId);

        service.delete(sampleId);

        verify(repository, times(1)).deleteById(sampleId);
    }
}