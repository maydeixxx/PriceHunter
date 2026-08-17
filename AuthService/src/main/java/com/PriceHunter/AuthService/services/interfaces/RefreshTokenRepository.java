package com.PriceHunter.AuthService.services.interfaces;

import com.PriceHunter.AuthService.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHashAndExpiresAtAfterAndRevokedFalse(String refreshTokenHash);
}
