package com.example.handler.data;

import com.example.handler.api.models.Orderr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderrRepository
    extends JpaRepository<Orderr, Long> {
}
