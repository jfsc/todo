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

/**
 * Tests for the {@link TodoRequest} and {@link TodoResponse} DTO classes.  These
 * objects are used to convert user input into the domain model and to
 * serialise the domain model back out over HTTP.  Testing ensures that
 * fields are correctly propagated and that validation constraints are
 * enforced.
 */
class TodoDtoTest {

    @Test
    void testTodoRequestToDomain() {
        TodoRequest req = new TodoRequest();
        req.setTitle("Write tests");
        req.setDescription("Ensure high coverage");
        req.setDone(true);

        Todo domain = req.toDomain();
        assertEquals("Write tests", domain.getTitle());
        assertEquals("Ensure high coverage", domain.getDescription());
        assertTrue(domain.isDone());

        // ensure a new id is generated
        assertNotNull(domain.getId());
    }

    @Test
    void testTodoRequestValidationNotBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        TodoRequest req = new TodoRequest();
        req.setTitle(""); // vazio, garantidamente inválido
        req.setDescription("Missing title");

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty(), "Title should not be blank");
    }

    @Test
    void testTodoResponseFromDomain() {
        UUID id = UUID.randomUUID();
        Todo domain = new Todo(id, "Implement feature", "Add new endpoint", false);
        Instant now = Instant.now();
        domain.setCreatedAt(now);
        domain.setUpdatedAt(now);

        TodoResponse response = TodoResponse.from(domain);
        assertEquals(id, response.getId());
        assertEquals("Implement feature", response.getTitle());
        assertEquals("Add new endpoint", response.getDescription());
        assertFalse(response.isDone());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getUpdatedAt());
    }
}