package com.example.handler.data;

import com.example.handler.api.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository
    extends JpaRepository<Dish, Long> {
}
