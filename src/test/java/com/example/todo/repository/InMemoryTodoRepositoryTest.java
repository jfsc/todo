package com.example.todo.repository;

import com.example.todo.domain.Todo;
import com.example.todo.usecase.TodoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTodoRepositoryTest {

    private InMemoryTodoRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryTodoRepository();
    }

    @Test
    void shouldCreateTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Create Test", "Teste de Criacao", false);
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals("Create Test", found.get().getTitle());
    }


      @Test
      void shouldListTodos() {
    	Todo todo = new Todo(UUID.randomUUID(), "List Test", "Teste de Lista", false);
        repository.save(todo);
        
        List<Todo> foundTodos = repository.findAll();
        assertTrue(!foundTodos.isEmpty());
      }


      @Test
      void shouldUpdateTodo() {
    	      	  
        Todo todo = new Todo(UUID.randomUUID(), "Update Test", "Teste de Update", false);
        repository.save(todo);
        
        Todo updatedTodo = repository.findById(todo.getId()).orElseThrow(() -> new RuntimeException("Não foi possível encontrar a tarefa."));
        
        updatedTodo.setTitle("Update Test (2)");
        updatedTodo.setDescription("Teste de Update Modificado");
        updatedTodo.setDone(true);
        repository.save(todo);
        
        assertNotEquals("Update Test", repository.findById(todo.getId()).get().getTitle());
        assertEquals(todo.getId(), updatedTodo.getId());
      }

      @Test
      void shouldDeleteTodo() {

        Todo todo = new Todo(UUID.randomUUID(), "Update Test", "Teste de Update", false);
        repository.save(todo);
        
        Todo foundTodo = repository.findById(todo.getId()).orElseThrow(() -> new RuntimeException("Não foi possível encontrar a tarefa."));
        repository.deleteById(foundTodo.getId());
        
        assertTrue(repository.findById(todo.getId()).isEmpty());	  
    }
}