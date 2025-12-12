package com.example.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void defaultConstructorShouldInitializeCorrectly() {
        Todo t = new Todo();

        assertNotNull(t.getId(), "ID should be auto-generated");
        assertEquals("", t.getTitle());
        assertEquals("", t.getDescription());
        assertFalse(t.isDone());
    }

    @Test
    void constructorWithIdTitleDescriptionDoneShouldWork() {
        UUID id = UUID.randomUUID();
        Todo t = new Todo(id, "Title", "Desc", true);

        assertEquals(id, t.getId());
        assertEquals("Title", t.getTitle());
        assertEquals("Desc", t.getDescription());
        assertTrue(t.isDone());
    }

    @Test
    void constructorWithTitleAndDescriptionShouldWork() {
        Todo t = new Todo("My Title", "My Desc");

        assertNotNull(t.getId());
        assertEquals("My Title", t.getTitle());
        assertEquals("My Desc", t.getDescription());
        assertFalse(t.isDone());
    }

    @Test
    void constructorWithIdTitleDoneShouldWork() {
        UUID id = UUID.randomUUID();
        Todo t = new Todo(id, "Title Only", true);

        assertEquals(id, t.getId());
        assertEquals("Title Only", t.getTitle());
        assertEquals("", t.getDescription());
        assertTrue(t.isDone());
    }

    @Test
    void settersShouldUpdateFieldsCorrectly() {
        Todo t = new Todo();

        UUID newId = UUID.randomUUID();
        t.setId(newId);
        t.setTitle("New Title");
        t.setDescription("New Desc");
        t.setDone(true);

        assertEquals(newId, t.getId());
        assertEquals("New Title", t.getTitle());
        assertEquals("New Desc", t.getDescription());
        assertTrue(t.isDone());
    }

    @Test
    void createdAtAndUpdatedAtShouldBeSettable() {
        Todo t = new Todo();

        Instant now = Instant.now();
        Instant later = now.plusSeconds(60);

        t.setCreatedAt(now);
        t.setUpdatedAt(later);

        assertEquals(now, t.getCreatedAt());
        assertEquals(later, t.getUpdatedAt());
    }

    @Test
    void randomUuidShouldBeDifferentForDefaultConstructor() {
        Todo t1 = new Todo();
        Todo t2 = new Todo();

        assertNotEquals(t1.getId(), t2.getId(), "UUIDs must be unique");
    }
}