package com.example.todo.dto;

import com.example.todo.domain.Todo;

import java.time.Instant;
import java.util.UUID;

public class TodoResponse {
    private UUID id;
    private String title;
    private String description;
    private boolean done;
    private Instant createdAt;
    private Instant updatedAt;

    public static TodoResponse from(Todo todo) {
        TodoResponse response = new TodoResponse();
        response.id = todo.getId(); response.title = todo.getTitle(); response.description = todo.getDescription();
        response.done = todo.isDone(); response.createdAt = todo.getCreatedAt(); response.updatedAt = todo.getUpdatedAt();
        return response;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isDone() { return done; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
