package com.example.todo.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TomlEnvironmentPostProcessorTest {

  @Autowired
  private Environment environment;

  @Test
  void postProcessEnvironment_shouldLoadTomlProperties() {
    String property = environment.getProperty("test.property.from.toml");

    assertEquals("Hello from TOML test!", property);
  }
}
