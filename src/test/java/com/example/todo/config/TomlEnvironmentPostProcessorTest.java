package com.example.todo.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.*;
import org.springframework.core.io.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class TomlEnvironmentPostProcessorTest {

    private final TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor();

    static class FakeResource extends ByteArrayResource {
        private final boolean exists;

        FakeResource(String content, boolean exists) {
            super(content.getBytes());
            this.exists = exists;
        }

        @Override
        public boolean exists() {
            return exists;
        }
    }

    @Test
    void shouldDoNothingWhenFileDoesNotExist() {
        ConfigurableEnvironment env = new StandardEnvironment();

        TomlEnvironmentPostProcessor.setOverrideResource(
                new FakeResource("", false)
        );

        processor.postProcessEnvironment(env, new SpringApplication());

        assertFalse(env.getPropertySources().contains("tomlPropertySource"));
    }

    @Test
    void shouldLoadTomlWhenExists() {
        ConfigurableEnvironment env = new StandardEnvironment();

        TomlEnvironmentPostProcessor.setOverrideResource(
                new FakeResource("""
                    port = 8081
                    name = "MyApp"
                """, true)
        );

        processor.postProcessEnvironment(env, new SpringApplication());

        assertEquals("8081", env.getProperty("port"));
        assertEquals("MyApp", env.getProperty("name"));
    }

    @Test
    void shouldCatchMalformedToml() {
        ConfigurableEnvironment env = new StandardEnvironment();

        TomlEnvironmentPostProcessor.setOverrideResource(
                new FakeResource("invalid = = =", true)
        );

        assertDoesNotThrow(() ->
                processor.postProcessEnvironment(env, new SpringApplication())
        );
    }

    @Test
    void shouldCatchExceptionInInputStream() {
        ConfigurableEnvironment env = new StandardEnvironment();

        Resource broken = new ByteArrayResource("test".getBytes()) {
            @Override
            public boolean exists() { return true; }

            @Override
            public InputStream getInputStream() throws IOException {
                throw new IOException("boom");
            }
        };

        TomlEnvironmentPostProcessor.setOverrideResource(broken);

        assertDoesNotThrow(() ->
                processor.postProcessEnvironment(env, new SpringApplication())
        );
    }
}
