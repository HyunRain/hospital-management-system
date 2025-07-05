package com.hms.auth_service.repository;

import com.hms.auth_service.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByUserEmail(String userEmail);
    void deleteByUserEmail(String email);
}
