package com.example.todo.config;

import com.moandjiezana.toml.Toml;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Map;

public class TomlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String NAME = "tomlPropertySource";

    protected Resource loadResource() {
        return new ClassPathResource("confd_spring/app_config.toml");
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            Resource res = loadResource();
            if (!res.exists()) {
                System.out.println("TOML config not found: confd_spring/app_config.toml");
                return;
            }

            try (InputStream is = res.getInputStream()) {
                Toml toml = new Toml().read(is);
                Map<String, Object> map = toml.toMap();

                MutablePropertySources sources = environment.getPropertySources();
                sources.addFirst(new MapPropertySource(NAME, map));

                System.out.println("TOML config loaded: " + map.keySet());
            }
        } catch (Exception e) {
            System.err.println("Failed to load TOML config: " + e.getMessage());
        }
    }

    public static String getName() { return NAME; }
}