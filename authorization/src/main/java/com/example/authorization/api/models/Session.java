package com.example.authorization.api.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @SequenceGenerator(
            name = "session_sequence",
            sequenceName = "session_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "session_sequence"
    )
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    private String sessionToken;
    private Timestamp expiresAt;

    public Session() {
    }

    public Session(User user, String sessionToken, Timestamp expiresAt) {
        this.user = user;
        this.sessionToken = sessionToken;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }


}
