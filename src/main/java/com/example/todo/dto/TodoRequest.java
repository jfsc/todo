package com.example.todo.dto;

import com.example.todo.domain.Todo;
import jakarta.validation.constraints.NotBlank;

public class TodoRequest {

    @NotBlank
    private String title;

    private String description;

    private boolean done;

    // Construtor vazio necessário para @RequestBody
    public TodoRequest() {}

    // Getters e Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    // Converte para domínio Todo
    public Todo toDomain() {
        Todo t = new Todo();
        t.setTitle(this.title);
        t.setDescription(this.description);
        t.setDone(this.done);
        return t;
    }
}
