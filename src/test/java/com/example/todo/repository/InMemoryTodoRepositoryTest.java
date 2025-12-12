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
	void shouldListTodos() {
		Todo todo1 = new Todo(UUID.randomUUID(), "Test 1", "teste de listagem 1", false);
		Todo todo2 = new Todo(UUID.randomUUID(), "Test 2", "teste de listagem 2", true);
		repository.save(todo1);
		repository.save(todo2);

		List<Todo> todos = repository.findAll();
		assertNotNull(todos);
		assertEquals(2, todos.size());
	}

	@Test
	void shouldUpdateTodo() {
		Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de atualização", false);
		repository.save(todo);

		todo.setTitle("Test Updated");
		todo.setDone(true);
		repository.save(todo);

		Optional<Todo> found = repository.findById(todo.getId());
		assertTrue(found.isPresent());
		assertEquals("Test Updated", found.get().getTitle());
		assertTrue(found.get().isDone());
	}

	@Test
	void shouldDeleteTodo() {
		Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de exclusão", false);
		repository.save(todo);

		repository.deleteById(todo.getId());
		Optional<Todo> found = repository.findById(todo.getId());
		assertFalse(found.isPresent());
	}

	@Test
	void shouldReturnEmptyListWhenNoTodos() {
		List<Todo> todos = repository.findAll();
		assertNotNull(todos);
		assertTrue(todos.isEmpty());
	}

	@Test
	void shouldReturnEmptyOptionalForNonExistentId() {
		Optional<Todo> found = repository.findById(UUID.randomUUID());
		assertTrue(found.isEmpty());
	}

	@Test
	void shouldDoNothingWhenDeletingNonExistentId() {
		Todo todo = new Todo(UUID.randomUUID(), "Test", "Ainda existe", false);
		repository.save(todo);

		assertDoesNotThrow(() -> {
			repository.deleteById(UUID.randomUUID());
		});

		assertEquals(1, repository.findAll().size());
	}

	@Test
	void shouldOverwriteTodoWithSameId() {
		UUID id = UUID.randomUUID();
		Todo originalTodo = new Todo(id, "Original", "Original Desc", false);
		repository.save(originalTodo);

		Todo overwritingTodo = new Todo(id, "Overwritten", "Overwritten Desc", true);
		repository.save(overwritingTodo);

		assertEquals(1, repository.findAll().size());
		Optional<Todo> found = repository.findById(id);
		assertTrue(found.isPresent());
		assertEquals("Overwritten", found.get().getTitle());
		assertTrue(found.get().isDone());
	}
}