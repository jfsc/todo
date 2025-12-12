package com.example.todo;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class TodoApplicationTest {

    @Test
    void main_shouldCallApplicationRun() {

        String[] args = new String[]{};

        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {

            TodoApplication.main(args);
            
            mocked.verify(() -> SpringApplication.run(TodoApplication.class, args), Mockito.times(1));
        }
    }
}