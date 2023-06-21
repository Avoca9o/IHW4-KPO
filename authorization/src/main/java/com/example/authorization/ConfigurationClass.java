package com.example.authorization;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationClass {
    @Bean
    CommandLineRunner commandLineRunner(
    ) {
        return args -> {
        };
    }
}
