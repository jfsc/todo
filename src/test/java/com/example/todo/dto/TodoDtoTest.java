package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoDtoTest {

    @Test
    void shouldConvertTodoRequestToDomainModel() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Write unit tests");
        request.setDescription("Achieve high code coverage");
        request.setDone(true);

        Todo domainTodo = request.toDomain();

        assertEquals("Write unit tests", domainTodo.getTitle());
        assertEquals("Achieve high code coverage", domainTodo.getDescription());
        assertTrue(domainTodo.isDone());

        assertNotNull(domainTodo.getId());
    }

    @Test
    void shouldFailValidationWhenTitleIsBlank() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        TodoRequest request = new TodoRequest();
        request.setTitle("");
        request.setDescription("Title is missing");

        Set<ConstraintViolation<TodoRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty(), "Title must not be blank");
    }

    @Test
    void shouldCreateTodoResponseFromDomainModel() {
        UUID todoId = UUID.randomUUID();
        Todo domainTodo = new Todo(
                todoId,
                "Implement feature",
                "Create a new API endpoint",
                false
        );

        Instant timestamp = Instant.now();
        domainTodo.setCreatedAt(timestamp);
        domainTodo.setUpdatedAt(timestamp);

        TodoResponse response = TodoResponse.from(domainTodo);

        assertEquals(todoId, response.getId());
        assertEquals("Implement feature", response.getTitle());
        assertEquals("Create a new API endpoint", response.getDescription());
        assertFalse(response.isDone());
        assertEquals(timestamp, response.getCreatedAt());
        assertEquals(timestamp, response.getUpdatedAt());
    }
}
