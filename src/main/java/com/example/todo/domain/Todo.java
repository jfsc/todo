package com.example.todo.domain;

import java.time.Instant;
import java.util.UUID;

public class Todo {
    private UUID id;
    private String title;
    private String description;
    private boolean done;
    private Instant createdAt;
    private Instant updatedAt;

    public Todo(UUID id, String title, String description, boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;

        // Permite instanciar classes com titulo vazio. CORREÇAO:
        // if (title == null || title.trim().isEmpty()) {
        //     throw new IllegalArgumentException("Titulo nao pode ser vazio ou nulo");
        // }
    }

    public Todo() {
        this.id = UUID.randomUUID();
        this.title = "";
        this.description = "";
        this.done = false;
    }

    public Todo(String title, String description) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.done = false;
    }

    public Todo(UUID id, String title, boolean done) {
        this.id = id;
        this.title = title;
        this.description = "";
        this.done = done;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
