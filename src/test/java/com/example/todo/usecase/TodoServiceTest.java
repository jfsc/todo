package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {


    @Mock
    private InMemoryTodoRepository repository;

    @InjectMocks
    private TodoService service;



    @Test
    void shouldFindExistingTodo() {
        UUID id = UUID.randomUUID();
        Todo existing = new Todo(id, "Existing", "Desc", false);

        when(repository.findById(id)).thenReturn(Optional.of(existing));

        Optional<Todo> result = service.find(id);

        assertTrue(result.isPresent());
        assertEquals("Existing", result.get().getTitle());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenTodoNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<Todo> result = service.find(nonExistentId);

        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    void shouldListAllTodos() {
        Todo todo1 = new Todo(UUID.randomUUID(), "T1", "D1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "T2", "D2", true);
        List<Todo> mockList = Arrays.asList(todo1, todo2);

        when(repository.findAll()).thenReturn(mockList);

        List<Todo> result = service.list();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    // --- (Create) ---

    @Test
    void shouldCreateTodoAndReturnSavedInstance() {
        Todo newTodo = new Todo(null, "New", "Desc", false);
        Todo savedTodo = new Todo(UUID.randomUUID(), "New", "Desc", false);

        when(repository.save(any(Todo.class))).thenReturn(savedTodo);

        Todo created = service.create(newTodo);

        assertNotNull(created.getId());
        assertEquals("New", created.getTitle());
        verify(repository, times(1)).save(newTodo);
    }

    // --- (Update) ---

    @Test
    void shouldUpdateExistingTodoSuccessfully() {
        UUID id = UUID.randomUUID();
        Todo existing = new Todo(id, "Old Title", "Old Desc", false);
        Todo updateData = new Todo(null, "New Title", "New Desc", true);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Todo updated = service.update(id, updateData);

        assertEquals("New Title", updated.getTitle());
        assertTrue(updated.isDone());
        assertEquals(id, updated.getId()); // O ID deve ser mantido

        verify(repository, times(1)).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenTodoNotFoundOnUpdate() {
        UUID nonExistentId = UUID.randomUUID();
        Todo updateData = new Todo(null, "Title", "Desc", false);


        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.update(nonExistentId, updateData);
        });

        assertEquals("Not found", exception.getMessage());
        verify(repository, never()).save(any(Todo.class)); // O save nunca deve ser chamado
    }

    // --- (Delete) ---

    @Test
    void shouldDeleteExistingTodoSuccessfully() {
        UUID existingId = UUID.randomUUID();
        Todo existing = new Todo(existingId, "Test", "Desc", false);


        when(repository.findById(existingId)).thenReturn(Optional.of(existing));

        service.delete(existingId);


        verify(repository, times(1)).findById(existingId);

        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    void shouldThrowExceptionWhenTodoNotFoundOnDelete() {
        UUID nonExistentId = UUID.randomUUID();


        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.delete(nonExistentId);
        });

        assertEquals("Not found", exception.getMessage());


        verify(repository, never()).deleteById(any(UUID.class));
    }
}
