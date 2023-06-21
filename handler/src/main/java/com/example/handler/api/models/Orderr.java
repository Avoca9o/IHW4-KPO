package com.example.handler.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "orderrs")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Orderr {
    @Id
    @SequenceGenerator(
            name = "orderr_sequence",
            sequenceName = "orderr_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orderr_sequence"
    )
    private Long id;
    private Long userId;
    private String status;
    private String specialRequests;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @Transient
    private String dishes;

    public Orderr(Long userId, String status, String specialRequests, Timestamp createdAt, Timestamp updatedAt, String dishes) {
        this.userId = userId;
        this.status = status;
        this.specialRequests = specialRequests;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dishes = dishes;
    }
}
