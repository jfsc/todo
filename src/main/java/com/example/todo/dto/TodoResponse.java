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

    public static TodoResponse from(Todo t) {
        if (t == null) return null; // Tratando erro nulo

        TodoResponse r = new TodoResponse();
        r.id = t.getId(); r.title = t.getTitle(); r.description = t.getDescription();
        r.done = t.isDone(); r.createdAt = t.getCreatedAt(); r.updatedAt = t.getUpdatedAt();
        return r;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isDone() { return done; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
