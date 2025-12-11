package com.example.todo.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;
import com.example.todo.domain.Todo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoResponseTest {

    @Test
    void from_shouldConvertTodoToResponseCorrectly() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Todo todo = new Todo(id,"Test title","Test description",true);

        TodoResponse response = TodoResponse.from(todo);

        assertEquals(todo.getId(), response.getId());
        assertEquals(todo.getTitle(), response.getTitle());
        assertEquals(todo.getDescription(), response.getDescription());
        assertEquals(todo.isDone(), response.isDone());
        assertEquals(todo.getCreatedAt(), response.getCreatedAt());
        assertEquals(todo.getUpdatedAt(), response.getUpdatedAt());
    }
}