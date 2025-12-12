package com.example.todo.controller;

import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void testFullFlow() {
        // POST
        TodoRequest req = new TodoRequest();
        req.setTitle("X");
        req.setDescription("Y");
        req.setDone(false);

        ResponseEntity<String> createRes = rest.postForEntity(url("/api/todos"), req, String.class);
        assertEquals(HttpStatus.CREATED, createRes.getStatusCode());
        URI loc = createRes.getHeaders().getLocation();

        // GET
        Todo saved = rest.getForEntity(loc, Todo.class).getBody();
        assertEquals("X", saved.getTitle());

        UUID id = saved.getId();

        // PUT
        req.setTitle("Z");
        rest.put(url("/api/todos/" + id), req);

        Todo updated = rest.getForEntity(loc, Todo.class).getBody();
        assertEquals("Z", updated.getTitle());

        // DELETE
        rest.delete(url("/api/todos/" + id));

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                rest.getForEntity(loc, String.class).getStatusCode()
        );
    }
}

