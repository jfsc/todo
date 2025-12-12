package com.example.todo.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class TomlEnvironmentPostProcessorTest {

  private ConfigurableEnvironment environment;

  @BeforeEach
  void setUp() {
    environment = new MockEnvironment();
  }

  @Test
  void shouldLoadTomlConfigWhenFileExists() {
    TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor("confd_spring/app_config.toml");

    processor.postProcessEnvironment(environment, null);

    assertThat(environment.getPropertySources().contains("tomlPropertySource")).isTrue();
    MapPropertySource propertySource = (MapPropertySource) environment.getPropertySources().get("tomlPropertySource");
    Assertions.assertNotNull(propertySource);
  }

  @Test
  void shouldNotAddPropertySourceWhenTomlFileDoesNotExist() {
    TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor("non_existent_file.toml");

    processor.postProcessEnvironment(environment, null);

    assertThat(environment.getPropertySources().contains("tomlPropertySource")).isFalse();
  }

  @Test
  void shouldNotAddPropertySourceWhenTomlFileIsMalformed() {
    TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor("confd_spring/malformed_app_config.toml");

    processor.postProcessEnvironment(environment, null);

    assertThat(environment.getPropertySources().contains("tomlPropertySource")).isFalse();
  }
}