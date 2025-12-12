package com.example.todo.repository;

import com.example.todo.domain.Todo;
import com.example.todo.exception.IdNullException;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryTodoRepository {
    private final Map<UUID, Todo> store = new ConcurrentHashMap<>();

    public Todo save(Todo todo) {
        if (todo.getId() == null)
            todo.setId(UUID.randomUUID());
        Instant now = Instant.now();
        if (todo.getCreatedAt() == null)
            todo.setCreatedAt(now);
        todo.setUpdatedAt(now);
        store.put(todo.getId(), todo);
        return todo;
    }

    public Optional<Todo> findById(UUID id) {
        if (Objects.isNull(id))
            throw new IdNullException("Todo ID must not be null.", 400);
        return Optional.ofNullable(store.get(id));
    }

    public List<Todo> findAll() {
        return store.values().stream().collect(Collectors.toList());
    }

    public void deleteById(UUID id) {
        store.remove(id);
    }
}
