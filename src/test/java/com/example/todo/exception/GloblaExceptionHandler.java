package com.example.todo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    // Controller fake apenas para disparar a exception
    @RestController
    static class TestController {
        @GetMapping("/explode")
        public String explode() {
            throw new RuntimeException("Item não encontrado");
        }
    }

    @Test
    void testHandleRuntimeException() throws Exception {
        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mvc.perform(get("/explode")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Item não encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp", notNullValue())); // timestamp sempre muda
    }
}
