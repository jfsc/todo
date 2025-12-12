package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTodoRepositoryTest {

    private InMemoryTodoRepository repository;

    @BeforeEach
    void setup() { repository = new InMemoryTodoRepository(); }

    @Test
    void shouldCreateTodoWithValidData() {
        Todo todo = new Todo("Test", "Teste de criação");
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertNotNull(found.get().getId());
        assertNotNull(found.get().getTitle());
        assertNotNull(found.get().getDescription());
        assertNotNull(found.get().getCreatedAt());
        assertNotNull(found.get().getUpdatedAt());
        assertEquals("Test", found.get().getTitle());
        assertEquals("Teste de criação", found.get().getDescription());
    }

    @Test
    void shouldGenerateTimestampsOnNewTodoSave() {
        Todo todo = new Todo("Test", "teste de criacao");
        Instant beforeSave = Instant.now();
        Todo todoSalved = repository.save(todo);

        assertNotNull(todoSalved.getId(), "O ID deve ser gerado.");
        assertNotNull(todoSalved.getCreatedAt(), "O created deve ser preenchido.");
        assertNotNull(todoSalved.getUpdatedAt(), "O updated deve ser preenchido.");
        assertTrue(todoSalved.getCreatedAt().isAfter(beforeSave) || todoSalved.getCreatedAt().equals(beforeSave),
                "O createdAt deve ser preenchido no momento do save.");
    }

    @Test
    void shouldListAllExistingTodos() {
        Todo todo1 = new Todo(UUID.randomUUID(), "Test1", "teste de criacao1", false, null, null);
        repository.save(todo1);
        Todo todo2 = new Todo(UUID.randomUUID(), "Test2", "teste de criacao2", true, null, null);
        repository.save(todo2);

        List<Todo> todoList = repository.findAll();

        assertNotNull(todoList);
        assertEquals(2, todoList.size(), "Deve listar 2 todos");
    }

    @Test
    void shouldFindClientByExistingId() {
        Todo todo = new Todo("Test", "Teste de criação");
        repository.save(todo);

        Optional<Todo> todoSalved = repository.findById(todo.getId());

        assertTrue(todoSalved.isPresent());
        assertEquals(todo.getDescription(), todoSalved.get().getDescription());
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        UUID idTodo = UUID.randomUUID();

        Optional<Todo>  found = repository.findById(idTodo);

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldUpdateTodoData() {

        String titleOriginal = "Tarefa Antiga";
        String descriptionOriginal = "Descrição inicial";

        Todo todoOriginal = new Todo(titleOriginal, descriptionOriginal);
        repository.save(todoOriginal);

        String NewTitle = "Tarefa Atualizada";
        String NewDescription = "Descrição corrigida";

        Todo todoUpdated = new Todo(todoOriginal.getId(), NewTitle, NewDescription, false, null, null);
        repository.save(todoUpdated);

        Optional<Todo> found = repository.findById(todoUpdated.getId());
        assertTrue(found.isPresent());
        assertEquals(NewTitle, found.get().getTitle());
        assertEquals(NewDescription, found.get().getDescription());
    }

    @Test
    void shouldDeleteTodoById() {

        Todo todo = new Todo("Test", "teste de criacao");
        repository.save(todo);

        repository.deleteById(todo.getId());

        assertFalse(repository.findById(todo.getId()).isPresent(), "O item deveria ter sido deletado, mas ainda existe.");
    }


}