package com.example.todo.dto;

import com.example.todo.domain.Todo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class TodoRequestTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldUseAllSettersAndGetters() {
        TodoRequest req = new TodoRequest();

        req.setTitle("ABC");
        req.setDescription("Desc");
        req.setDone(true);

        assertEquals("ABC", req.getTitle());
        assertEquals("Desc", req.getDescription());
        assertTrue(req.isDone());
    }

    @Test
    void shouldConvertToDomain() {
        TodoRequest req = new TodoRequest();
        req.setTitle("Task");
        req.setDescription("X");
        req.setDone(false);

        Todo todo = req.toDomain();

        assertNotNull(todo);
        assertEquals("Task", todo.getTitle());
        assertEquals("X", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void shouldValidateTitle() {
        TodoRequest req = new TodoRequest();
        req.setTitle(""); // inválido
        req.setDescription("x");
        req.setDone(false);

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
    }

}