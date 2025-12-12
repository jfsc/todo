package com.example.todo.config;


import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;


class TomlEnvironmentPostProcessorTest {

    private TomlEnvironmentPostProcessor processor;
    private ConfigurableEnvironment environment;

    @BeforeEach
    void setup() {
        processor = new TomlEnvironmentPostProcessor();
        environment = new StandardEnvironment();
    }

    @Test
    void testPostProcessEnvironmentAddsTomlProperties() {
        SpringApplication app = new SpringApplication();

        assertDoesNotThrow(() ->
                processor.postProcessEnvironment(environment, app)
        );

        // verifica se o PropertySource foi registrado
        assertNotNull(
                environment.getPropertySources().get("tomlPropertySource"),
                "O PropertySource TOML deveria estar presente"
        );
    }
}