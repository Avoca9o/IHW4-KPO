package com.example.authorization.api.controllers;

import com.example.authorization.api.models.User;
import com.example.authorization.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user, user.getPassword(), user.getRoles());
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().equals("Email already exists")) {
                return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Invalid email")) {
                return new ResponseEntity<>("Invalid email. It should contains only latin letters and digits, one @ and one . in the domain",
                        HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Invalid password")) {
                return new ResponseEntity<>("Invalid password. It should be of the length from 4 to 30", HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Wrong roles")) {
                return new ResponseEntity<>("Invalid roles. Example: \"admin,user,vacuumcleaner\"", HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Empty name")) {
                return new ResponseEntity<>("Name shouldn't be empty", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Something strange happened", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Something strange happened", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        try {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/info/{token}")
    public ResponseEntity<?> getUserInfo(@PathVariable("token") String token) {
        try {
            return new ResponseEntity<>(userService.getUserInfo(token), HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().equals("Invalid token")) {
                return new ResponseEntity<>("No active session with such token", HttpStatus.BAD_REQUEST );
            }
            return new ResponseEntity<>("Something strange happened on request", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
