package com.example.todo.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.mockito.Mockito.*;

class TomlEnvironmentPostProcessorTest {

    @Test
    void testFileNotExists() {
        ConfigurableEnvironment env = mock(ConfigurableEnvironment.class);
        SpringApplication app = mock(SpringApplication.class);

        // Simula arquivo que não existe
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor() {
            @Override
            protected Resource getResource() {
                return new ClassPathResource("arquivo_inexistente.toml") {
                    @Override
                    public boolean exists() {
                        return false;
                    }
                };
            }
        };

        processor.postProcessEnvironment(env, app);
        // O método deve retornar sem lançar exceção
    }

    @Test
    void testExceptionDuringRead() throws Exception {
        ConfigurableEnvironment env = mock(ConfigurableEnvironment.class);
        SpringApplication app = mock(SpringApplication.class);

        // Mock do Resource para lançar exceção ao tentar abrir o InputStream
        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenThrow(new RuntimeException("Simulated IO exception"));

        // Subclasse para injetar o Resource mockado
        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor() {
            @Override
            protected Resource getResource() {
                return mockResource;
            }
        };

        // Chamada do método, catch interno deve ser acionado sem propagar exceção
        processor.postProcessEnvironment(env, app);
    }

    @Test
    void testFileExistsAndLoaded() throws Exception {
        ConfigurableEnvironment env = mock(ConfigurableEnvironment.class);
        SpringApplication app = mock(SpringApplication.class);

        // Resource real (arquivo pode existir ou usar mock do InputStream)
        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/dummy.toml"));

        TomlEnvironmentPostProcessor processor = new TomlEnvironmentPostProcessor() {
            @Override
            protected Resource getResource() {
                return mockResource;
            }
        };

        processor.postProcessEnvironment(env, app);
        // Cobre o branch normal de carregamento
    }
}