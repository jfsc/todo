package com.example.todo.dto;

import com.example.todo.domain.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoRequestTest {

    @Test
    void shouldSetAndGetTitle() {
        TodoRequest request = new TodoRequest();
        
        request.setTitle("Test Title");
        
        assertEquals("Test Title", request.getTitle());
    }

    @Test
    void shouldSetAndGetDescription() {
        TodoRequest request = new TodoRequest();
        
        request.setDescription("Test Description");
        
        assertEquals("Test Description", request.getDescription());
    }

    @Test
    void shouldSetAndGetDone() {
        TodoRequest request = new TodoRequest();
        
        request.setDone(true);
        
        assertTrue(request.isDone());
    }

     @Test
     void shouldConvertToDomain() {
         TodoRequest request = new TodoRequest();
         request.setTitle("My Title");
         request.setDescription("My Description");
         request.setDone(true);

         Todo todo = request.toDomain();

         assertNotNull(todo);
         assertNotNull(todo.getId());
         assertEquals("My Title", todo.getTitle());
         assertEquals("My Description", todo.getDescription());
         assertTrue(todo.isDone());
     }

     @Test
     void shouldConvertToDomainWithNullDescription() {
         TodoRequest request = new TodoRequest();
         request.setTitle("My Title");
         request.setDone(false);

         Todo todo = request.toDomain();

         assertNotNull(todo);
         assertEquals("My Title", todo.getTitle());
         assertNull(todo.getDescription());
         assertFalse(todo.isDone());
     }
}