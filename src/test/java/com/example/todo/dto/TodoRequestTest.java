package com.example.todo.dto;

import com.example.todo.domain.Todo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TodoRequestTest {

    @Autowired
    private Validator validator;

    @Test
    void toDomain_shouldMapCorrectly(){
        TodoRequest req = new TodoRequest();
        req.setTitle("Papitest");
        req.setDescription("Testing if to domain transfer a DTO into a Todo (object)");
        req.setDone(false);

        Todo domain = req.toDomain();

        assertEquals("Papitest", domain.getTitle());
        assertEquals("Testing if to domain transfer a DTO into a Todo (object)", domain.getDescription());
        assertFalse(domain.isDone());
    }

    @Test
    void shouldFailInValidationWhenTitleIsBlank(){
        TodoRequest req = new TodoRequest();
        req.setTitle("");
        req.setDescription("test");
        req.setDone(true);

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty(), "Spring validator should alert when title is empty");
    }

    @Test
    void shouldFailInValidationWhenTitleIsNull(){
        TodoRequest req = new TodoRequest();
        req.setTitle(null);
        req.setDescription("test");
        req.setDone(true);

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty(), "Spring validator should alert when title is null");
    }

    @Test
    void shouldPassValidationWhenTitleIsValid(){
        TodoRequest req = new TodoRequest();
        req.setTitle("Title");
        req.setDescription("test");
        req.setDone(true);

        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(req);

        assertTrue(violations.isEmpty(), "Spring validator shouldn't alert when title is valid");
    }


}
