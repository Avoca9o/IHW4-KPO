package com.example.handler.api.controllers;

import com.example.handler.api.models.Dish;
import com.example.handler.services.DishService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    private static class Token {
        private String token;
        public Token(String token) {
            this.token = token;
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getMenu() {
        try {
            return new ResponseEntity<>(dishService.getMenu(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something strange happened", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    private static class Context{
        private Dish dish;
        private String token;

        public Context(Dish dish, String token) {
            this.dish = dish;
            this.token = token;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDishInfo(@PathVariable("id") Long idx, @RequestBody Token token) {
        try {
            return new ResponseEntity<>(dishService.getDishInfo(token.getToken(), idx), HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalAccessException iae) {
            return new ResponseEntity<>("You're not an admin", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createDish(@RequestBody Context context) {
        try {
            dishService.createDish(context.getToken(), context.getDish());
            return new ResponseEntity<>("Dish created successfully", HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalAccessException iae) {
            return new ResponseEntity<>("You're not an admin", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDish(@PathVariable("id") Long idx, @RequestBody Context context) {
        try {
            dishService.updateDish(context.getToken(), idx, context.getDish());
            return new ResponseEntity<>("Dish updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalAccessException iae) {
            return new ResponseEntity<>("You're not an admin", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable("id") Long idx, @RequestBody Token token) {
        try {
            dishService.deleteDish(token.getToken(), idx);
            return new ResponseEntity<>("Dish deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalAccessException iae) {
            return new ResponseEntity<>("You're not an admin", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
