package com.example.authorization.service;

import com.example.authorization.api.models.Session;
import com.example.authorization.api.models.User;
import com.example.authorization.data.SessionRepository;
import com.example.authorization.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public UserService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public void registerUser(User user, String password, String roles) throws Exception {
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        for (User user1 : userRepository.findAll()) {
            if (user1.getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
//        if (user.getUsername() == null || user.getUsername().isEmpty()) {
//            throw new IllegalArgumentException("Empty name");
//        }
        if (roles.isEmpty()) {
            user.setRoles("user");
        } else {
            List<String> r = Arrays.stream(roles.split(",")).toList();
            for (String s : r) {
                for (var ch : s.toCharArray()) {
                    if (!Character.isAlphabetic(ch)) {
                        throw new IllegalArgumentException("Invalid roles");
                    }
                }
            }
            user.setRoles(roles);
        }
        user.setPasswordHash(password.hashCode());
        user.setCreatedAt(new Timestamp(new Date().getTime()));
        user.setUpdatedAt(user.getCreatedAt());
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserInfo(String token) {
        for (Session s : sessionRepository.findAll()) {
            if (s.getSessionToken().equals(token)) {
                return userRepository.findById(s.getUser().getId()).get();
            }
        }
        throw new IllegalArgumentException("Invalid token");
    }

    private boolean isValidEmail(String email) {
        int counterAt = 0;
        int counterPoint = 0;
        for (var ch : email.toCharArray()) {
            if (ch == '@') {
                ++counterAt;
            } else if (ch == '.') {
                if (counterAt == 0) {
                    return false;
                }
                ++counterPoint;
            } else if (!(Character.isAlphabetic(ch) || Character.isDigit(ch))) {
                return false;
            }
        }
        if (counterPoint > 1 || counterAt > 1) {
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 4 && password.length() <= 30;
    }
}
