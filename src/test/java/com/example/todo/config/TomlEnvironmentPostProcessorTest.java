package com.example.todo.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TomlEnvironmentPostProcessorTest {

    @Mock
    private ConfigurableEnvironment environment;
    @Mock
    private SpringApplication application;
    @Mock
    private MutablePropertySources propertySources;
    @Mock
    private Resource mockResource;

    private TomlEnvironmentPostProcessor processor;

    @BeforeEach
    void setUp() {
        // Create a subclass of the processor that overrides getResource for testing
        processor = new TomlEnvironmentPostProcessor() {
            @Override
            protected Resource getResource(String path) {
                return mockResource;
            }
        };
    }

    @Test
    void shouldLoadTomlAndAddPropertySource() throws IOException {
        String tomlContent = "test_property = \"value123\""; // Use a simple, valid TOML key
        InputStream is = new ByteArrayInputStream(tomlContent.getBytes());

        when(environment.getPropertySources()).thenReturn(propertySources);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(is);

        processor.postProcessEnvironment(environment, application);

        ArgumentCaptor<MapPropertySource> captor = ArgumentCaptor.forClass(MapPropertySource.class);
        verify(propertySources).addFirst(captor.capture());

        MapPropertySource capturedSource = captor.getValue();
        assertEquals("tomlPropertySource", capturedSource.getName());
        assertEquals("value123", capturedSource.getProperty("test_property"));
    }

    @Test
    void shouldDoNothingWhenResourceDoesNotExist() {
        when(mockResource.exists()).thenReturn(false);

        processor.postProcessEnvironment(environment, application);

        verify(environment, never()).getPropertySources();
    }
    
    @Test
    void shouldHandleIOExceptionGracefully() throws IOException {
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenThrow(new IOException("Test IO Exception"));

        // The exception should be caught and logged to System.err, not thrown
        processor.postProcessEnvironment(environment, application);

        verify(environment, never()).getPropertySources();
    }
}
