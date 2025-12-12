package com.example.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodoApplicationTest {

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context can start successfully.
    }

    @Test
    void main() {
        // This test calls the main method to improve code coverage.
        TodoApplication.main(new String[]{});
    }
}
