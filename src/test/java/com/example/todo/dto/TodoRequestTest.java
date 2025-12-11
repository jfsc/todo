package com.example.todo.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.example.todo.domain.Todo;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TodoRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        TodoRequest request = new TodoRequest();

        request.setTitle("Task 1");
        request.setDescription("Desc");
        request.setDone(true);

        assertEquals("Task 1", request.getTitle());
        assertEquals("Desc", request.getDescription());
        assertTrue(request.isDone());
    }

    @Test
    void testToDomainConversion() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Study");
        request.setDescription("Study Java");
        request.setDone(false);

        Todo todo = request.toDomain();

        assertEquals("Study", todo.getTitle());
        assertEquals("Study Java", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void testTitleNotBlankValidation() {
        TodoRequest request = new TodoRequest();
        request.setTitle("");
        request.setDescription("Some description");
        request.setDone(false);

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }
}
