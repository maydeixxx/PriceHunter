package com.PriceHunter.AuthService.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString(exclude = {"tokenHash"})
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @Column(updatable = false, nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
