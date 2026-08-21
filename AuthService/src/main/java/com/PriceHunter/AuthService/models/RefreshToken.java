package com.PriceHunter.AuthService.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@ToString(exclude = {"tokenHash"})
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String tokenHash;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean revoked;
}
