package com.example.todo.usecase;

import com.example.todo.InMemoryTodoRepository;
import com.example.todo.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    @Mock
    private InMemoryTodoRepository repository;

    @InjectMocks
    private TodoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Todo t = new Todo();
        when(repository.save(t)).thenReturn(t);

        Todo result = service.create(t);

        assertEquals(t, result);
        verify(repository).save(t);
    }

    @Test
    void testFind() {
        UUID id = UUID.randomUUID();
        Todo t = new Todo();

        when(repository.findById(id)).thenReturn(Optional.of(t));

        Optional<Todo> result = service.find(id);

        assertTrue(result.isPresent());
        assertEquals(t, result.get());
    }

    @Test
    void testList() {
        List<Todo> list = List.of(new Todo(), new Todo());
        when(repository.findAll()).thenReturn(list);

        List<Todo> result = service.list();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testUpdateSuccess() {
        UUID id = UUID.randomUUID();

        Todo existing = new Todo();
        existing.setTitle("Old");
        existing.setDescription("Old Desc");
        existing.setDone(false);

        Todo update = new Todo();
        update.setTitle("New");
        update.setDescription("New Desc");
        update.setDone(true);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Todo result = service.update(id, update);

        assertEquals("New", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        assertTrue(result.isDone());

        verify(repository).save(existing);
    }

    @Test
    void testUpdateNotFound() {
        UUID id = UUID.randomUUID();
        Todo update = new Todo();

        when(repository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.update(id, update)
        );

        assertEquals("Not found", ex.getMessage());
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo();
        when(repository.findById(id)).thenReturn(Optional.of(todo));
        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void testDelete_NotFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.delete(id));

        verify(repository, never()).deleteById(any());
    }



}
