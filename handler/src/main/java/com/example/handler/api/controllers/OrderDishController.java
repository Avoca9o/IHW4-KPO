package com.example.handler.api.controllers;

import com.example.handler.services.OrderDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order-dishes")
public class OrderDishController {
    private final OrderDishService orderDishService;

    @Autowired
    public OrderDishController(OrderDishService orderDishService) {
        this.orderDishService = orderDishService;
    }
}
