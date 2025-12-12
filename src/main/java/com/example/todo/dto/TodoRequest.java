package com.example.todo.dto;

import com.example.todo.domain.Todo;

import jakarta.validation.constraints.NotBlank;

public class TodoRequest {
    @NotBlank
    private String title;
    private String description;
    private boolean done;


    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDone(boolean done) { this.done = done; }

    public Todo toDomain() {
        Todo t = new Todo();
        t.setTitle(this.title);
        t.setDescription(this.description);
        t.setDone(this.done);
        return t;
    }
}
