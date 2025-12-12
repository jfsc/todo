package com.example.todo.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TomlEnviromentPostProcessorTest {

    @Test
    void shouldLoadTomlAndAddPropertySource() {
        StandardEnvironment environment = new StandardEnvironment();
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor();

        processor.postProcessEnvironment(environment, mock(SpringApplication.class));

        assertEquals("Todo API", environment.getProperty("title"));
        assertTrue(Boolean.parseBoolean(environment.getProperty("enableMetrics")));
        assertEquals(8080, Integer.parseInt(environment.getProperty("serverPort")));
    }

    @Test
    void shouldNotThrowWhenEnvironmentPropertySourcesThrows() {
        ConfigurableEnvironment brokenEnv = mock(ConfigurableEnvironment.class);
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor();

        assertDoesNotThrow(() -> processor.postProcessEnvironment(brokenEnv, mock(SpringApplication.class)));
    }

    @Test
    void shouldIgnoreWhenTomlResourceNotFound() {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        ClassLoader hiding = new ClassLoader(original) {
            @Override
            public java.net.URL getResource(String name) {
                if ("confd_spring/app_config.toml".equals(name) || name.endsWith("confd_spring/app_config.toml")) {
                    return null;
                }
                return super.getResource(name);
            }

            @Override
            public java.io.InputStream getResourceAsStream(String name) {
                if ("confd_spring/app_config.toml".equals(name) || name.endsWith("confd_spring/app_config.toml")) {
                    return null;
                }
                return super.getResourceAsStream(name);
            }
        };

        try {
            Thread.currentThread().setContextClassLoader(hiding);

            StandardEnvironment environment = new StandardEnvironment();
            TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor();

            processor.postProcessEnvironment(environment, mock(SpringApplication.class));

            assertNull(environment.getProperty("title"));
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }
}