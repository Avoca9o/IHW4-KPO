package com.example.handler.api.models;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
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
@Table(name = "dishes")
public class Dish {
    @Id
    @SequenceGenerator(
            name = "dish_sequence",
            sequenceName = "dish_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "dish_sequence"
    )
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long quantity;

    public Dish(String name, String description, Double price, Long quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }
}
