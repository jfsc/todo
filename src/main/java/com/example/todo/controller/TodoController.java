package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.usecase.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;  // <-- CORRETO

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@Validated
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TodoResponse> list() {
        return service.list().stream().map(TodoResponse::from).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoRequest req) {
        Todo created = service.create(req.toDomain());
        return ResponseEntity.created(URI.create("/api/todos/" + created.getId()))
                .body(TodoResponse.from(created));
    }

    @GetMapping("/{id}")
    public TodoResponse get(@PathVariable("id")UUID id) {
        return service.find(id).map(TodoResponse::from)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PutMapping("/{id}")
    public TodoResponse update(@PathVariable ("id")UUID id, @Valid @RequestBody TodoRequest req) {
        return TodoResponse.from(service.update(id, req.toDomain()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id")UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
