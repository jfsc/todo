package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryTodoRepository {
    private final Map<UUID, Todo> store = new ConcurrentHashMap<>();

    public Todo save(Todo todo) {
        if (todo.getId() == null) todo.setId(UUID.randomUUID());
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("America/Sao_Paulo"));
        if (todo.getCreatedAt() == null) todo.setCreatedAt(now);
        todo.setUpdatedAt(now);
        store.put(todo.getId(), todo);
        return todo;
    }

    public Optional<Todo> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
    public List<Todo> findAll() { return store.values().stream().collect(Collectors.toList()); }
    public void deleteById(UUID id) { store.remove(id); }
}
