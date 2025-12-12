package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TodoTest {

    @Test
    void noArgsConstructorShouldCreateTodoWithDefaultValues() {
        Todo todo = new Todo();
        assertThat(todo.getId()).isNotNull();
        assertThat(todo.getTitle()).isEqualTo("");
        assertThat(todo.getDescription()).isEqualTo("");
        assertThat(todo.isDone()).isFalse();
    }

    @Test
    void titleAndDescriptionConstructorShouldSetValuesCorrectly() {
        String title = "My Title";
        String description = "My Description";
        Todo todo = new Todo(title, description);

        assertThat(todo.getId()).isNotNull();
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getDescription()).isEqualTo(description);
        assertThat(todo.isDone()).isFalse();
    }

    @Test
    void idTitleAndDoneConstructorShouldSetValuesCorrectly() {
        UUID id = UUID.randomUUID();
        String title = "My Title";
        boolean done = true;
        Todo todo = new Todo(id, title, done);

        assertThat(todo.getId()).isEqualTo(id);
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getDescription()).isEqualTo("");
        assertThat(todo.isDone()).isEqualTo(done);
    }
    
    @Test
    void fullArgsConstructorShouldSetValuesCorrectly() {
        UUID id = UUID.randomUUID();
        String title = "Full Title";
        String description = "Full Description";
        boolean done = true;
        Todo todo = new Todo(id, title, description, done);

        assertThat(todo.getId()).isEqualTo(id);
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getDescription()).isEqualTo(description);
        assertThat(todo.isDone()).isEqualTo(done);
    }

    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        Todo todo = new Todo();
        UUID id = UUID.randomUUID();
        String title = "New Title";
        String description = "New Description";
        boolean done = true;
        Instant now = Instant.now();

        todo.setId(id);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDone(done);
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);

        assertThat(todo.getId()).isEqualTo(id);
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getDescription()).isEqualTo(description);
        assertThat(todo.isDone()).isEqualTo(done);
        assertThat(todo.getCreatedAt()).isEqualTo(now);
        assertThat(todo.getUpdatedAt()).isEqualTo(now);
    }
}
