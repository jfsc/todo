package com.example.todo.dto;

import com.example.todo.domain.Todo;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

public class TodoRequestTest {

    @Autowired
    private static Validator validator;

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
    void

}
