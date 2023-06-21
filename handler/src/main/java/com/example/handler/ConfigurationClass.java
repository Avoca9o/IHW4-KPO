package com.example.handler;

import com.example.handler.api.models.Dish;
import com.example.handler.data.DishRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConfigurationClass {
    @Bean
    CommandLineRunner commandLineRunner(
            DishRepository dishRepository
    ) {
        return args -> {

            Dish latte = new Dish("latte", "coffee with a lot of milk", 4.99, 100L);
            Dish burger = new Dish("cheeseburger", "a big burger", 5.99, 60L);
            Dish cola = new Dish("cola", "unhealthy drink", 3.55, 44L);
            Dish shwarm = new Dish("shwarm", "meal of Gods", 5.00, 2L);

            dishRepository.saveAll(List.of(latte, burger, cola, shwarm));
        };
    }
}
