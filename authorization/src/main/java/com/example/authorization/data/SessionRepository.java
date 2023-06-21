package com.example.authorization.data;

import com.example.authorization.api.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository
    extends JpaRepository<Session, Long> {
}
