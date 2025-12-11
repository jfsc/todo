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
    void shouldCreateTodoNull() {
        Todo todoNull = new Todo(null, null, null, false);
        repository.save(todoNull);
        Optional<Todo> found = repository.findById(todoNull.getId());
        assertTrue(found.isPresent());

    }

    @Test
    void shouldListTodos() {

        Todo todo1 = new Todo(UUID.randomUUID(),"Learn JUnit5", "You should learn JUnit", false);
        Todo todo2 = new Todo(UUID.randomUUID(),"Finish logic in Industrialist", "This should be make in Figma", false);
        Todo todo3 = new Todo(UUID.randomUUID(),"Learn to use AI", "Learn how to learn fast with AI", true);

        repository.save(todo1);
        repository.save(todo2);
        repository.save(todo3);

        List<Todo> todos = repository.findAll();

        assertEquals(3,todos.size());
        assertTrue(todos.contains(todo1));
        assertTrue(todos.contains(todo2));
        assertTrue(todos.contains(todo3));

    }


    @Test
    void shoulFindTodoById() {

        Todo todo = new Todo(UUID.randomUUID(),"Finish logic in Industrialist", "This should be make in Figma", false);

        repository.save(todo);

        Optional<Todo> todoFound = repository.findById(todo.getId());

        assertTrue(todoFound.isPresent());

    }

    @Test
    void shouldUpdateTodo() {
        Todo todo1 = new Todo(UUID.randomUUID(),"Learn JUnit5", "You should learn JUnit", false);
        repository.save(todo1);

        todo1.setTitle("Finish logic in Industrialist");
        todo1.setDescription("Updated description");
        todo1.setDone(true);

        repository.save(todo1);

        Optional<Todo> updated = repository.findById(todo1.getId());

        assertTrue(updated.isPresent());
        assertEquals("Finish logic in Industrialist", updated.get().getTitle());
        assertEquals("Updated description", updated.get().getDescription());
        assertTrue(updated.get().isDone());

  }

    @Test
    void shouldDeleteTodo() {

        Todo todo = new Todo(UUID.randomUUID(), "Finish logic in Industrialist", "This should be make in Figma", false);
        repository.save(todo);
        Optional<Todo> found = repository.findById(todo.getId());
        assertTrue(found.isPresent());


        repository.deleteById(todo.getId());
        Optional<Todo> deleted = repository.findById(todo.getId());

        assertFalse(deleted.isPresent());
    }

}