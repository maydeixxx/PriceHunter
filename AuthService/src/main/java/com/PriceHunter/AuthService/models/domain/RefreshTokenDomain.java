package com.PriceHunter.AuthService.models.domain;

import com.PriceHunter.AuthService.models.exceptions.RefreshTokenArgumentException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RefreshTokenDomain {
    private final UUID id;

    private final UUID userId;
    private final String email;

    private final String tokenHash;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private Boolean revoked;

    private RefreshTokenDomain(UUID id, UUID userId, String email, String tokenHash, LocalDateTime createdAt, LocalDateTime expiresAt, Boolean revoked) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.tokenHash = tokenHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    public static RefreshTokenDomain createRefreshToken(UUID userId, String email, String tokenHash, LocalDateTime createdAt, LocalDateTime expiresAt) {
        if (userId == null) {
            throw new RefreshTokenArgumentException("User id cant be null");
        }

        if (email == null) {
            throw new RefreshTokenArgumentException("Email id cant be null");
        }

        if (tokenHash == null) {
            throw new RefreshTokenArgumentException("Token hash cant be null");
        }

        if (createdAt == null) {
            throw new RefreshTokenArgumentException("CreatedAt cant be null");
        }

        if (expiresAt == null) {
            throw new RefreshTokenArgumentException("ExpiresAt cant be null");
        }

        UUID uuid = UUID.randomUUID();
        return new RefreshTokenDomain(uuid, userId, email, tokenHash, createdAt, expiresAt, false);
    }

    public void revokeToken() {
        if (this.revoked) {
            throw new RefreshTokenArgumentException("Token already revoked");
        }
        this.revoked = true;
    }
}
