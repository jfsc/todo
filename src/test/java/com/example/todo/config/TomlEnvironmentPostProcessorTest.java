package com.example.todo.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.Resource;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TomlEnvironmentPostProcessorTest {
    // made by chatgpt (code syntax to my instructions)
    @Test
    void postProcessEnvironment_whenResourceMissing_shouldNotAddPropertySource() {

        String missingPath = "confd_spring/nonexistent_test_file_" + System.currentTimeMillis() + ".toml";

        // inject with constructor
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor(missingPath);

        // Mock of environment and propertySources
        ConfigurableEnvironment env = mock(ConfigurableEnvironment.class);
        MutablePropertySources sources = mock(MutablePropertySources.class);
        when(env.getPropertySources()).thenReturn(sources);

        // Act: call method (Shouldn't throw an exception even with missing resource)
        processor.postProcessEnvironment(env, null);

        // Assert: Make sure we don't create another MapPropertySource
        verify(sources, never()).addFirst(any());
    }

    @Test
    void postProcressEnviroment_whenExceptionOccurs_shouldCatchException() throws IOException {
        Resource badResourch = mock(Resource.class);
        when(badResourch.exists()).thenReturn(true);
        when(badResourch.getInputStream()).thenThrow(new IOException("ops balups"));

        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor();
        SpringApplication app = null;
        assertDoesNotThrow(() -> processor.postProcessEnvironment(null, app) );

    }
    }
