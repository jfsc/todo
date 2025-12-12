package com.example.todo.dto;

import com.example.todo.domain.Todo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TodoRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateTitleNotBlank() {
        TodoRequest req = new TodoRequest();
        req.setTitle(""); // inválido (NotBlank)
        req.setDescription("desc");
        req.setDone(false);

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title"))
        );
    }

    @Test
    void shouldConvertToDomainCorrectly() {
        TodoRequest req = new TodoRequest();
        req.setTitle("Teste");
        req.setDescription("Descrição");
        req.setDone(true);

        Todo domain = req.toDomain();

        assertEquals("Teste", domain.getTitle());
        assertEquals("Descrição", domain.getDescription());
        assertTrue(domain.isDone());
    }
}