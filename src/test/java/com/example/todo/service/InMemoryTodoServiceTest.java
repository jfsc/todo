package com.example.todo.service;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTodoServiceTest {

    private TodoService service;

    @BeforeEach
    void setup() {
        service = new TodoService(new InMemoryTodoRepository());
    }

    @Test
    void shouldSaveTodo() {
        Todo todo = new Todo("Test", "Teste de criação");

        Todo newTodo = service.create(todo);

        assertNotNull(newTodo);
    }

    @Test
    void shouldReturnTodoWhenIdExist() {
        Todo todo = new Todo("Test", "Teste de criação");
        service.create(todo);

        Optional<Todo> todoExist = service.find(todo.getId());

        assertTrue(todoExist.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
        UUID idNonExistent = UUID.randomUUID();

        Optional<Todo> todo = service.find(idNonExistent);

        assertTrue(todo.isEmpty());
    }

    @Test
    void shouldReturnAllTodos() {
        Todo todo1 = new Todo("Test1", "teste de criacao1");
        service.create(todo1);
        Todo todo2 = new Todo("Test2", "teste de criacao2");
        service.create(todo2);

        List<Todo> todoList = service.list();

        assertNotNull(todoList);
        assertTrue(todoList.contains(todo1));
        assertTrue(todoList.contains(todo2));
        assertEquals(2, todoList.size());
    }

    @Test
    void shouldUpdateFieldsWhenIdExists() {
        Todo todo = new Todo("Test1", "teste de criacao1");
        service.create(todo);

        Todo todoUpdate = new Todo("Teste para atualizar", "teste de atualizacao");
        service.update(todo.getId(), todoUpdate);

        Optional<Todo> newTodo = service.find(todo.getId());
        assertTrue(newTodo.isPresent());
        assertEquals(newTodo.get(), todo);
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExistOnUpdate() {
        UUID idNonExistent = UUID.randomUUID();

        Todo todoUpdate = new Todo("Teste para atualizar", "teste de atualizacao");

        assertThrows(RuntimeException.class, () -> service.update(idNonExistent, todoUpdate));

    }

    @Test
    void shouldRemoveTodoWhenIdExists() {
        Todo todo = new Todo("Test1", "teste de criacao1");
        service.create(todo);

        service.delete(todo.getId());

        assertFalse(service.find(todo.getId()).isPresent());

    }
}
