package com.example.authorization.api.controllers;

import com.example.authorization.service.SessionService;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class SearchRequest {
        private String email;

        private String password;

        public SearchRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewSession(@RequestBody SearchRequest sr) {
        try {
            String jwt = sessionService.createSession(sr.getEmail(), sr.getPassword());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().equals("User not found")) {
                return new ResponseEntity<>("User not found with such email", HttpStatus.BAD_REQUEST);
            } else if (iae.getMessage().equals("Wrong password")) {
                return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Something strange happened", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Something strange happened", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
