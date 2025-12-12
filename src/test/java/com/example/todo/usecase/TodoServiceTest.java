package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    private TodoService service;
    private InMemoryTodoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTodoRepository();
        service = new TodoService(repository);
    }

    @Test
    void testCreate() {
        Todo todo = new Todo();
        todo.setTitle("Estudar Java");
        todo.setDescription("Estudar testes unitários");
        todo.setDone(false);

        Todo created = service.create(todo);


        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Estudar Java", created.getTitle());
        assertEquals("Estudar testes unitários", created.getDescription());
        assertFalse(created.isDone());
    }

    @Test
    void testFind() {
        Todo todo = new Todo();
        todo.setTitle("Teste");
        todo.setDescription("Descrição teste");
        Todo saved = service.create(todo);

        Optional<Todo> found = service.find(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Teste", found.get().getTitle());
    }

    @Test
    void testFindNotFound() {
        UUID randomId = UUID.randomUUID();

        Optional<Todo> found = service.find(randomId);

        assertFalse(found.isPresent());
    }

    @Test
    void testList() {
        Todo todo1 = new Todo();
        todo1.setTitle("Todo 1");
        service.create(todo1);

        Todo todo2 = new Todo();
        todo2.setTitle("Todo 2");
        service.create(todo2);

        List<Todo> list = service.list();

        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void testUpdate() {
        Todo todo = new Todo();
        todo.setTitle("Título Original");
        todo.setDescription("Descrição Original");
        todo.setDone(false);
        Todo saved = service.create(todo);

        Todo update = new Todo();
        update.setTitle("Título Atualizado");
        update.setDescription("Descrição Atualizada");
        update.setDone(true);


        Todo updated = service.update(saved.getId(), update);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Título Atualizado", updated.getTitle());
        assertEquals("Descrição Atualizada", updated.getDescription());
        assertTrue(updated.isDone());
    }

    @Test
    void testUpdateNotFound() {
        UUID randomId = UUID.randomUUID();
        Todo update = new Todo();
        update.setTitle("Teste");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.update(randomId, update);
        });

        assertEquals("Not found", exception.getMessage());
    }

    @Test
    void testDelete() {

        Todo todo = new Todo();
        todo.setTitle("Para deletar");
        Todo saved = service.create(todo);

        service.delete(saved.getId());
        Optional<Todo> found = service.find(saved.getId());

        assertFalse(found.isPresent());
    }

    @Test
    void testListEmpty() {

        List<Todo> list = service.list();

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}