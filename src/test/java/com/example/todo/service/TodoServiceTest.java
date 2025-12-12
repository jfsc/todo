package com.example.todo.service;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private InMemoryTodoRepository repository;

    @InjectMocks
    private TodoService service;

    @Test
    void shouldCreateTodo() {
        Todo send = new Todo(null, "Title", "Description", false);
        Todo saveTodo = new Todo(UUID.randomUUID(), "Title", "Description", false);

        when(repository.save(send)).thenReturn(saveTodo);

        Todo result = service.create(send);

        assertNotNull(result.getId());
        assertEquals("Title", result.getTitle());
        verify(repository).save(send);
    }

    @Test
    void shouldDeleteTodo() {
        UUID id = UUID.randomUUID();
        Todo deleteTodo = new Todo(id, "Delete Test", "Teste do Delete", false);

        when(repository.findById(id)).thenReturn(Optional.of(deleteTodo));
        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void shouldReturnTodo() {
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "Return Test", "Teste de Retorno", false);

        when(repository.findById(id)).thenReturn(Optional.of(todo));
        Optional<Todo> result = service.find(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void shouldListTodo() {
        Todo todo1 = new Todo(UUID.randomUUID(), "List Test (1)", "Teste de Listagem (1)", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "List Test (2)", "Teste de Listagem (2)", false);

        List<Todo> todoList = List.of(todo1, todo2);

        when(repository.findAll()).thenReturn(todoList);

        List<Todo> result = service.list();

        assertEquals(2, result.size());
        assertEquals("List Test (1)", result.get(0).getTitle());
        assertEquals("List Test (2)", result.get(1).getTitle());

        verify(repository).findAll();
    }

    @Test
    void shouldUpdateTodo() {
        UUID id = UUID.randomUUID();

        Todo existingTodo = new Todo(id, "Update Test (1)", "Teste de Update (1)", false);
        Todo updateTodo = new Todo(id, "Update Test (2)", "Teste de Update (2)", true);

        when(repository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(repository.save(existingTodo)).thenReturn(existingTodo);

        Todo result = service.update(id, updateTodo);

        assertEquals("Update Test (2)", result.getTitle());
        assertEquals("Teste de Update (2)", result.getDescription());
        assertTrue(result.isDone());

        verify(repository).save(existingTodo);
    }


}
