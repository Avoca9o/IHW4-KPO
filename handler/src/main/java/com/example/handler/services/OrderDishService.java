package com.example.handler.services;

import com.example.handler.data.OrderDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDishService {
    private final OrderDishRepository orderDishRepository;

    @Autowired
    public OrderDishService(OrderDishRepository orderDishRepository) {
        this.orderDishRepository = orderDishRepository;
    }
}
