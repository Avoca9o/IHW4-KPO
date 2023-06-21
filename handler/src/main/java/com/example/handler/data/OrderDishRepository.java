package com.example.handler.data;

import com.example.handler.api.models.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDishRepository
    extends JpaRepository<OrderDish, Long> {
}
