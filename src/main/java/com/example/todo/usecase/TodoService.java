package com.example.todo.usecase;

import com.example.todo.domain.Todo;
import com.example.todo.InMemoryTodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoService {
    private final InMemoryTodoRepository repo;
    public TodoService(InMemoryTodoRepository repo) { this.repo = repo; }

    public Todo create(Todo t) {
        return repo.save(t);
    }

    public Optional<Todo> find(UUID id) {
        return repo.findById(id);
    }

    public List<Todo> list() {
        return repo.findAll();
    }

    public Todo update(UUID id, Todo update) {
        Todo existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        existing.setTitle(update.getTitle());
        existing.setDescription(update.getDescription());
        existing.setDone(update.isDone());
        return repo.save(existing);
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }
}
