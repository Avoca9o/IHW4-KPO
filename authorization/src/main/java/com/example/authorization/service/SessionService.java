package com.example.authorization.service;

import com.example.authorization.api.models.Session;
import com.example.authorization.api.models.User;
import com.example.authorization.data.SessionRepository;
import com.example.authorization.data.UserRepository;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static net.oauth.jsontoken.JsonToken.AUDIENCE;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public String createSession(String email, String password) throws Exception {
        User user = null;
        for (User u : userRepository.findAll()) {
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (user.getPasswordHash() != password.hashCode()) {
            throw new IllegalArgumentException("Wrong password");
        }
        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);

        Calendar cal = Calendar.getInstance();
        HmacSHA256Signer signer;
        try {
            signer = new HmacSHA256Signer("avo", null, sharedSecret);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        JsonToken token = new net.oauth.jsontoken.JsonToken(signer);
        token.setAudience(AUDIENCE);
        token.setIssuedAt(new org.joda.time.Instant(cal.getTimeInMillis()));
        token.setExpiration(new org.joda.time.Instant(cal.getTimeInMillis() + 1000L * 3L));

        var expirationTime = new Timestamp(new Date().getTime() + 3000 * 60);
        sessionRepository.save(new Session(user, token.toString(), expirationTime));
        return token.toString();
    }
}
