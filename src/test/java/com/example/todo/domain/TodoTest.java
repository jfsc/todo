package com.example.todo.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void defaultConstructorShouldInitializeWithRandomIdAndDefaults() {
        Todo todo = new Todo();
        assertNotNull(todo.getId());
        assertEquals("", todo.getTitle());
        assertEquals("", todo.getDescription());
        assertFalse(todo.isDone());
        assertNull(todo.getCreatedAt()); // CreatedAt and UpdatedAt are set by repository
        assertNull(todo.getUpdatedAt()); // CreatedAt and UpdatedAt are set by repository
    }

    @Test
    void constructorWithTitleAndDescriptionShouldInitializeCorrectly() {
        String title = "Test Title";
        String description = "Test Description";
        Todo todo = new Todo(title, description);
        assertNotNull(todo.getId());
        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void fullConstructorShouldInitializeAllFields() {
        UUID id = UUID.randomUUID();
        String title = "Full Title";
        String description = "Full Description";
        boolean done = true;
        Todo todo = new Todo(id, title, description, done);
        assertEquals(id, todo.getId());
        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
        assertTrue(todo.isDone());
    }

    @Test
    void constructorWithIdTitleAndDoneShouldInitializeCorrectly() {
        UUID id = UUID.randomUUID();
        String title = "Id Title Done";
        boolean done = true;
        Todo todo = new Todo(id, title, done);
        assertEquals(id, todo.getId());
        assertEquals(title, todo.getTitle());
        assertEquals("", todo.getDescription()); // Default description should be empty
        assertTrue(todo.isDone());
    }


    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        Todo todo = new Todo();
        UUID id = UUID.randomUUID();
        String title = "Setter Title";
        String description = "Setter Description";
        boolean done = true;
        Instant now = Instant.now();

        todo.setId(id);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDone(done);
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);

        assertEquals(id, todo.getId());
        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
        assertEquals(done, todo.isDone());
        assertEquals(now, todo.getCreatedAt());
        assertEquals(now, todo.getUpdatedAt());
    }
}