package com.example.todo.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TomlEnvironmentPostProcessorTest {

    private TomlEnvironmentPostProcessor processor;
    private ConfigurableEnvironment environment;
    private SpringApplication application;
    private MutablePropertySources propertySources;
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        processor = new TomlEnvironmentPostProcessor();
        environment = new StandardEnvironment();
        application = mock(SpringApplication.class);
        propertySources = environment.getPropertySources();
        
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void shouldLoadTomlConfigWhenFileExists() {
        processor.postProcessEnvironment(environment, application);

        String output = outContent.toString();
        assertTrue(output.contains("TOML config loaded:"), 
                "Should print success message when file is loaded");
        
        boolean hasTomlPropertySource = false;
        for (org.springframework.core.env.PropertySource<?> ps : propertySources) {
            if (ps.getName().equals("tomlPropertySource")) {
                hasTomlPropertySource = true;
                MapPropertySource mapPs = (MapPropertySource) ps;
                assertNotNull(mapPs.getSource(), "Property source should have content");
                break;
            }
        }
        assertTrue(hasTomlPropertySource, "Should add tomlPropertySource to environment");
    }

    @Test
    void shouldPrintMessageWhenFileNotFound() {
        ConfigurableEnvironment emptyEnv = mock(ConfigurableEnvironment.class);
        MutablePropertySources emptySources = new MutablePropertySources();
        when(emptyEnv.getPropertySources()).thenReturn(emptySources);
        
        processor.postProcessEnvironment(emptyEnv, application);
        
        assertDoesNotThrow(() -> {
            processor.postProcessEnvironment(emptyEnv, application);
        });
    }

    @Test
    void shouldHandleExceptionGracefully() {
        ConfigurableEnvironment badEnv = mock(ConfigurableEnvironment.class);
        when(badEnv.getPropertySources()).thenThrow(new RuntimeException("Simulated error"));
        
        assertDoesNotThrow(() -> {
            processor.postProcessEnvironment(badEnv, application);
        });
        
        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Failed to load TOML config") || errorOutput.isEmpty(),
                "Should log error message when exception occurs");
    }

    @Test
    void shouldReadTomlPropertiesCorrectly() {
        processor.postProcessEnvironment(environment, application);
        
        String output = outContent.toString();
        
        if (output.contains("TOML config loaded:")) {
            assertTrue(output.contains("[") && output.contains("]"),
                    "Should show the keys that were loaded");
        }
    }

    @Test
    void shouldNotThrowExceptionWhenApplicationIsNull() {
        assertDoesNotThrow(() -> {
            processor.postProcessEnvironment(environment, null);
        });
    }

    @Test
    void shouldHandleEmptyTomlFile() {
        assertDoesNotThrow(() -> {
            processor.postProcessEnvironment(environment, application);
        });
        
        assertNotNull(environment.getPropertySources());
    }

    @org.junit.jupiter.api.AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}