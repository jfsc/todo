package com.example.todo.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

class TomlEnvironmentPostProcessorTest {

    @Test
    void shouldNotThrowWhenExceptionHappensAndNotAddPropertySource() {
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor() {
            @Override
            protected Resource loadResource() {
                return new ByteArrayResource(new byte[0]) {
                    @Override
                    public boolean exists() {
                        return true;
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        throw new IOException("boom");
                    }
                };
            }
        };

        ConfigurableEnvironment environment = new StandardEnvironment();
        SpringApplication application = new SpringApplication(Object.class);

        assertDoesNotThrow(() -> processor.postProcessEnvironment(environment, application));
        assertFalse(environment.getPropertySources().contains(TomlEnvironmentPostProcessor.getName()));
    }

    @Test
    void shouldLoadTomlAndAddTomlPropertySourceAsFirst() {
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor();
        ConfigurableEnvironment environment = new StandardEnvironment();
        SpringApplication application = new SpringApplication(Object.class);

        processor.postProcessEnvironment(environment, application);

        MutablePropertySources sources = environment.getPropertySources();
        assertTrue(sources.contains("tomlPropertySource"));

        String firstName = sources.iterator().next().getName();
        assertEquals("tomlPropertySource", firstName);

        MapPropertySource source = (MapPropertySource) sources.get("tomlPropertySource");
        assertNotNull(source);
        assertTrue(source.getSource().containsKey("title"));
        assertTrue(source.getSource().containsKey("enableMetrics"));
        assertTrue(source.getSource().containsKey("serverPort"));

        assertEquals("Todo API", environment.getProperty("title"));
        assertEquals(true, environment.getProperty("enableMetrics", Boolean.class));
        assertEquals(8080, environment.getProperty("serverPort", Integer.class));
    }

    @Test
    void shouldLoadTomlAndAddPropertySource() {
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor();
        ConfigurableEnvironment environment = new StandardEnvironment();
        SpringApplication application = new SpringApplication(Object.class);

        processor.postProcessEnvironment(environment, application);

        MutablePropertySources sources = environment.getPropertySources();
        assertTrue(sources.contains(TomlEnvironmentPostProcessor.getName()));

        MapPropertySource source = (MapPropertySource) sources.get(TomlEnvironmentPostProcessor.getName());
        assertNotNull(source);
        assertEquals("Todo API", environment.getProperty("title"));
        assertEquals(true, environment.getProperty("enableMetrics", Boolean.class));
        assertEquals(8080, environment.getProperty("serverPort", Integer.class));
    }

    @Test
    void shouldDoNothingWhenTomlDoesNotExist() {
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor() {
            @Override
            protected Resource loadResource() {
                return new ByteArrayResource(new byte[0], "non-existing") {
                    @Override
                    public boolean exists() {
                        return false;
                    }
                };
            }
        };

        ConfigurableEnvironment environment = new StandardEnvironment();
        SpringApplication application = new SpringApplication(Object.class);

        processor.postProcessEnvironment(environment, application);

        assertFalse(environment.getPropertySources().contains(TomlEnvironmentPostProcessor.getName()));
    }
}
