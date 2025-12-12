package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryTodoRepository {

    private final Map<UUID, Todo> store = new ConcurrentHashMap<>();

    public Todo save(Todo todo) {
        store.put(todo.getId(), todo);
        return todo;
    }

    public Optional<Todo> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Todo> findAll() {
        return new ArrayList<>(store.values());
    }

    public void deleteById(UUID id) {
        store.remove(id);
    }
}
