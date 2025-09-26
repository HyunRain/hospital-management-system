package com.hms.auth_service.repository;

import com.hms.auth_service.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByUserEmail(String userEmail);
    void deleteByUserEmail(String email);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expirationTime < :time")
    void deleteAllExpiredSince(@Param("time") LocalDateTime time);
}
