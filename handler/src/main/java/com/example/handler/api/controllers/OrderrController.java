package com.example.handler.api.controllers;

import com.example.handler.api.models.Orderr;
import com.example.handler.services.OrderrService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.naming.EjbRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/orderrs")
public class OrderrController {
    private final OrderrService orderrService;
    @Autowired
    public OrderrController(OrderrService orderrService) {
        this.orderrService = orderrService;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    private static class OrderrRequest {

        private String dishes;
        private String specialRequests;

        public OrderrRequest(String dishes, String specialRequests) {
            this.dishes = dishes;
            this.specialRequests = specialRequests;
        }
    }

    @PostMapping("/{token}")
    public ResponseEntity<?> createOrderr(@RequestBody OrderrRequest orderrRequest, @PathVariable("token") String token) {
        try {
            orderrService.createOrderr(orderrRequest.getDishes(),
                    orderrRequest.getSpecialRequests(),
                    token);
            return new ResponseEntity<>("Order created successfully", HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().equals("No such user")) {
                return new ResponseEntity<>("No such user", HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Unknown dish")) {
                return new ResponseEntity<>("Request contains wrong dish id", HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Invalid parameters")) {
                return new ResponseEntity<>("Invalid dishes request. Example: <dishId>:<count>,<dishId>:<count>,..",
                        HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Empty order")) {
                return new ResponseEntity<>("You can't create an empty order", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Something strange happened with request", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderInfo(@PathVariable("id") Long idx) {
        try {
            return new ResponseEntity<>(orderrService.getOrderInfo(idx), HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().equals("Invalid index")) {
                return new ResponseEntity<>("Index is less than zero or not such order", HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Not enough goods")) {
                return new ResponseEntity<>("Not enough goods for order", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Something strange happened with arguments", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Something strange happened", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
