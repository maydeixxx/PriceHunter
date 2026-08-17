package com.PriceHunter.AuthService.models.domain;

import com.PriceHunter.AuthService.models.exceptions.RefreshTokenArgumentException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RefreshTokenDomain {
    private final UUID id;

    private final UUID userId;

    private final String tokenHash;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private Boolean revoked;

    private RefreshTokenDomain(UUID id, UUID userId, String tokenHash, LocalDateTime createdAt, LocalDateTime expiresAt, Boolean revoked) {
        this.id = id;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    public static RefreshTokenDomain createRefreshToken(UUID userId, String tokenHash, LocalDateTime createdAt, LocalDateTime expiresAt, Boolean revoked) {
        if (userId == null) {
            throw new RefreshTokenArgumentException("User id cant be null");
        }

        if (tokenHash == null) {
            throw new RefreshTokenArgumentException("Token hash cant be null");
        }

        if (createdAt == null) {
            throw new RefreshTokenArgumentException("createdAt cant be null");
        }

        if (expiresAt == null) {
            throw new RefreshTokenArgumentException("expiresAt cant be null");
        }

        if (revoked == null) {
            throw new RefreshTokenArgumentException("revoked cant be null");
        }

        UUID uuid = UUID.randomUUID();
        return new RefreshTokenDomain(uuid, userId, tokenHash, createdAt, expiresAt, revoked);
    }

    public void revokeToken() {
        this.revoked = true;
    }
}
