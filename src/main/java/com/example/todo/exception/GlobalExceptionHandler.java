package com.example.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

        // exceção global -> 500
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            Map<String,Object> body = Map.of(
                "type", "about:blank",
                "title", ex.getMessage(),
                "status", status.value(),
                "timestamp", Instant.now().toString()
            );
            return ResponseEntity.status(status).body(body);
        }

        @ExceptionHandler(IdNullException.class)
        public ResponseEntity<Map<String, Object>> handleIdNullException(IdNullException error) {
            HttpStatus status = HttpStatus.valueOf(error.getCode());
            Map<String,Object> body = Map.of(
                "type", "about:blank",
                "title", error.getMessage(),
                "status", status.value(),
                "timestamp", Instant.now().toString()
            );
            return ResponseEntity.status(status).body(body);
        }

        @ExceptionHandler(TodoNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleTodoNotFoundException(TodoNotFoundException error) {
            HttpStatus status = HttpStatus.valueOf(error.getCode());
            Map<String,Object> body = Map.of(
                "type", "about:blank",
                "title", error.getMessage(),
                "status", status.value(),
                "timestamp", Instant.now().toString()
            );
            return ResponseEntity.status(status).body(body);
        }
}
