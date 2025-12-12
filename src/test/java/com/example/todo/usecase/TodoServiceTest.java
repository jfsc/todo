package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TodoServiceTest {

  private TodoService service;
  private Todo todoTest;

  @BeforeEach
  void setup() {
    service = new TodoService(new InMemoryTodoRepository());
    todoTest = new Todo();
    todoTest.setId(UUID.randomUUID());
    todoTest.setTitle("Testando TodoService");
    todoTest.setDescription("Teste para validar a logica do servico");
    todoTest.setDone(false);
  }

  @Test
  void createTodo_shouldCreateTodo() {
    Todo createdTodo = service.create(todoTest);

    Assertions.assertNotNull(createdTodo.getId());
    Assertions.assertEquals(todoTest.getTitle(), createdTodo.getTitle());
  }

  @Test
  void findTodo_shouldFindTodo() {
    Todo createdTodo = service.create(todoTest);

    Optional<Todo> foundTodo = service.find(createdTodo.getId());

    Assertions.assertTrue(foundTodo.isPresent());
    Assertions.assertEquals(createdTodo.getId(), foundTodo.get().getId());
  }

  @Test
  void listTodo_shouldListAllTodo() {
    service.create(todoTest);

    List<Todo> todoList = service.list();

    Assertions.assertFalse(todoList.isEmpty());
    Assertions.assertEquals(1, todoList.size());
  }

  @Test
  void updateTodo_shouldAlterTodo() {
    Todo createdTodo = service.create(todoTest);
    Todo updatedInfo = new Todo();
    updatedInfo.setTitle("Título Atualizado");
    updatedInfo.setDescription("Descrição Atualizada");
    updatedInfo.setDone(true);

    service.update(createdTodo.getId(), updatedInfo);

    Optional<Todo> foundTodo = service.find(createdTodo.getId());
    Assertions.assertTrue(foundTodo.isPresent());
    Assertions.assertEquals("Título Atualizado", foundTodo.get().getTitle());
    Assertions.assertEquals("Descrição Atualizada", foundTodo.get().getDescription());
    Assertions.assertTrue(foundTodo.get().isDone());
  }

  @Test
  void deleteTodo_shouldDeleteTodo() {
    Todo createdTodo = service.create(todoTest);

    service.delete(createdTodo.getId());

    Optional<Todo> foundTodo = service.find(createdTodo.getId());
    Assertions.assertFalse(foundTodo.isPresent());
  }
}
