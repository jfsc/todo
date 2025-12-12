package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTodoRepositoryTest {

    private InMemoryTodoRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryTodoRepository();
        repository.clear();
    }

    @Test
    void shouldCreateTodo() {
        Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
        repository.save(todo);

        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getTitle());
    }


   @Test
   void shouldListTodos() {
	   Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
       Todo todo2 = new Todo(UUID.randomUUID(), "Test2", "teste de criacao", false);

	    repository.save(todo);
	    repository.save(todo2);

	    List<Todo> found = repository.findAll();

	    List<String> titles = found.stream()
	            .map(Todo::getTitle)
	            .toList();

	    assertTrue(titles.contains("Test"));
	    assertTrue(titles.contains("Test2"));
   }
   @Test
   void shouldUpdateTodo() {
	   Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
	    repository.save(todo);
	    Optional<Todo> found = repository.findById(todo.getId());
	    assertTrue(found.isPresent());
	    Todo existing = found.get();
	    existing.setTitle("Test2");
	    repository.save(existing);
	    Optional<Todo> updated = repository.findById(todo.getId());
	    assertTrue(updated.isPresent());
	    assertEquals("Test2", updated.get().getTitle());
   }

   @Test
   void shouldDeleteTodo() {
	   Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);
       repository.save(todo);

       repository.deleteById(todo.getId());
       
       Optional<Todo> found = repository.findById(todo.getId());
       assertTrue(!found.isPresent());
   }
}