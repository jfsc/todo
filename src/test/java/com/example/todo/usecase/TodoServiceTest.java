package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private InMemoryTodoRepository repo;

    @InjectMocks
    private TodoService service;

    private UUID id;
    private Todo todo;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        todo = new Todo();
        todo.setId(id);
        todo.setTitle("Test");
        todo.setDescription("Desc");
        todo.setDone(false);
    }

    // create()
    @Test
    void testCreate() {
        when(repo.save(any(Todo.class))).thenReturn(todo);

        Todo created = service.create(todo);

        assertNotNull(created);
        assertEquals("Test", created.getTitle());
        verify(repo, times(1)).save(todo);
    }

    // find()
    @Test
    void testFindExistingTodo() {
        when(repo.findById(id)).thenReturn(Optional.of(todo));

        Optional<Todo> found = service.find(id);

        assertTrue(found.isPresent());
        assertEquals(id, found.get().getId());
    }

    @Test
    void testFindNotFound() {
        when(repo.findById(id)).thenReturn(Optional.empty());

        Optional<Todo> found = service.find(id);

        assertFalse(found.isPresent());
    }

    // list()
    @Test
    void testList() {
        List<Todo> list = List.of(todo);
        when(repo.findAll()).thenReturn(list);

        List<Todo> result = service.list();

        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getTitle());
        verify(repo, times(1)).findAll();
    }

    // update()
    @Test
    void testUpdateSuccess() {
        Todo update = new Todo();
        update.setTitle("Updated Title");
        update.setDescription("Updated Desc");
        update.setDone(true);

        when(repo.findById(id)).thenReturn(Optional.of(todo));
        when(repo.save(any(Todo.class))).thenAnswer(inv -> inv.getArgument(0));

        Todo result = service.update(id, update);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Desc", result.getDescription());
        assertTrue(result.isDone());
        verify(repo).findById(id);
        verify(repo).save(todo);
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        Todo update = new Todo();
        when(repo.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.update(id, update);
        });

        assertEquals("Not found", ex.getMessage());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    // delete()
    @Test
    void testDelete() {
        doNothing().when(repo).deleteById(id);

        service.delete(id);

        verify(repo, times(1)).deleteById(id);
    }
}