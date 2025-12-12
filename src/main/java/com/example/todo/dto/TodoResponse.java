package com.example.todo.dto;

import com.example.todo.domain.Todo;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TodoResponse {
    private UUID id;
    private String title;
    private String description;
    private boolean done;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static TodoResponse from(Todo t) {
        TodoResponse r = new TodoResponse();
        r.id = t.getId(); r.title = t.getTitle(); r.description = t.getDescription();
        r.done = t.isDone(); r.createdAt = t.getCreatedAt(); r.updatedAt = t.getUpdatedAt();
        return r;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isDone() { return done; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
