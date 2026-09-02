package com.PriceHunter.AuthService.services.interfaces;

import com.PriceHunter.AuthService.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findRefreshTokenById(UUID tokenId);
    List<RefreshToken> findRefreshTokensByUserId(UUID userId);
}
