package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldConvertTodoRequestToDomain() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Test Title");
        request.setDescription("Test Description");
        request.setDone(true);

        Todo domain = request.toDomain();

        assertEquals("Test Title", domain.getTitle());
        assertEquals("Test Description", domain.getDescription());
        assertTrue(domain.isDone());
        assertNotNull(domain.getId());
    }

    @Test
    void shouldConvertDomainToTodoResponse() {
        Todo domain = new Todo(UUID.randomUUID(), "Test Title", "Test Description", true);
        domain.setCreatedAt(java.time.Instant.now());
        domain.setUpdatedAt(java.time.Instant.now());

        TodoResponse response = TodoResponse.from(domain);

        assertEquals(domain.getId(), response.getId());
        assertEquals(domain.getTitle(), response.getTitle());
        assertEquals(domain.getDescription(), response.getDescription());
        assertEquals(domain.isDone(), response.isDone());
        assertEquals(domain.getCreatedAt(), response.getCreatedAt());
        assertEquals(domain.getUpdatedAt(), response.getUpdatedAt());
    }

    @Test
    void whenTodoRequestTitleIsBlank_thenValidationFails() {
        TodoRequest request = new TodoRequest();
        request.setTitle(""); // Title is blank
        request.setDescription("Some description");

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        ConstraintViolation<TodoRequest> violation = violations.iterator().next();
        assertEquals("title", violation.getPropertyPath().toString());
        assertEquals("{jakarta.validation.constraints.NotBlank.message}", violation.getMessageTemplate());
    }

    @Test
    void whenTodoRequestTitleIsNull_thenValidationFails() {
        TodoRequest request = new TodoRequest();
        request.setTitle(null); // Title is null
        request.setDescription("Some description");

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void whenTodoRequestTitleIsValid_thenValidationSucceeds() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Valid Title");
        request.setDescription("Some description");

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}