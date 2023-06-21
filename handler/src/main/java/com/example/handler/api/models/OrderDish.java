package com.example.handler.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "order_dishes")
public class OrderDish {
    @Id
    @SequenceGenerator(
            name = "order_dish_sequence",
            sequenceName = "order_dish_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_dish_sequence"
    )
    private Long id;
    @JoinColumn(name = "orderr_id")
    private Long orderrId;
    @JoinColumn(name = "dish_id")
    private Long dishId;
    private Long quantity;
    private Double price;

    public OrderDish(Long orderrId, Long dishId, Long quantity) {
        this.orderrId = orderrId;
        this.dishId = dishId;
        this.quantity = quantity;
    }
}
