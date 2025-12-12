//-- BUG 01 --

/*O compilador Java não inclui os nomes dos parâmetros no bytecode por padrão. No entanto,
* o Spring Boot necessita do nome do parâmetro para fazer o binding. O Spring não sabe que
* {id} corresponde a id, ocasionando o erro "Name for argument of type [java.util.UUID]
* not specified, and parameter name information not available via reflection. Ensure that
* the compiler uses the '-parameters' flag." Para evitar o erro, deve-se passar a notação
* referente ao id da segunte forma: @PathVariable("id")*/

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
    public TodoResponse get(@PathVariable("id") UUID id) {
        return service.find(id).map(TodoResponse::from)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PutMapping("/{id}")
    public TodoResponse update(@PathVariable("id") UUID id, @Valid @RequestBody TodoRequest req) {
        return TodoResponse.from(service.update(id, req.toDomain()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }
}
