package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private InMemoryTodoRepository repo;

    @InjectMocks
    private TodoService service;

    @Test
    void shouldCreateTodo() {
        Todo todo = new Todo("New Todo", "Description");
        when(repo.save(any(Todo.class))).thenReturn(todo);

        Todo created = service.create(todo);

        assertNotNull(created);
        assertEquals("New Todo", created.getTitle());
        verify(repo, times(1)).save(todo);
    }

    @Test
    void shouldFindTodoById() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Found Todo", false);
        when(repo.findById(id)).thenReturn(Optional.of(todo));

        Optional<Todo> found = service.find(id);

        assertTrue(found.isPresent());
        assertEquals(id, found.get().getId());
        verify(repo, times(1)).findById(id);
    }

    @Test
    void shouldListAllTodos() {
        Todo todo1 = new Todo("Todo 1", "Desc 1");
        Todo todo2 = new Todo("Todo 2", "Desc 2");
        when(repo.findAll()).thenReturn(List.of(todo1, todo2));

        List<Todo> todos = service.list();

        assertNotNull(todos);
        assertEquals(2, todos.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void shouldUpdateTodo() {
        UUID id = UUID.randomUUID();
        Todo existingTodo = new Todo(id, "Original Title", false);
        Todo updatedTodoInfo = new Todo("Updated Title", "Updated Desc");
        updatedTodoInfo.setDone(true);

        when(repo.findById(id)).thenReturn(Optional.of(existingTodo));
        when(repo.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Todo result = service.update(id, updatedTodoInfo);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Desc", result.getDescription());
        assertTrue(result.isDone());
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(existingTodo);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTodo() {
        UUID id = UUID.randomUUID();
        Todo updatedTodoInfo = new Todo("Updated Title", "Updated Desc");
        updatedTodoInfo.setDone(true);
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.update(id, updatedTodoInfo);
        });

        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any(Todo.class));
    }

    @Test
    void shouldDeleteTodo() {
        UUID id = UUID.randomUUID();
        
        doNothing().when(repo).deleteById(id);
        service.delete(id);
        verify(repo, times(1)).deleteById(id);
    }
}