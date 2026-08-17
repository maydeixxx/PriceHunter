package com.PriceHunter.AuthService.models.domain;

import com.PriceHunter.AuthService.models.Role;
import com.PriceHunter.AuthService.models.exceptions.AuthArgumentException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class AuthDomain {
    private final UUID id;

    private final String email;

    private final String passwordHash;

    private final Role role;

    private Boolean enabled;
    private Boolean locked;

    private final LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthDomain that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    private AuthDomain(UUID id, String email, String passwordHash, Role role, Boolean enabled, Boolean locked, LocalDateTime createdAt, LocalDateTime lastLoginAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.enabled = enabled;
        this.locked = locked;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    public static AuthDomain createAuthModel(String email, String passwordHash, Role role, Boolean enabled, Boolean locked, LocalDateTime createdAt, LocalDateTime lastLoginAt) {
        if (email == null || email.isBlank()) {
            throw new AuthArgumentException("email cant be blank or null");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new AuthArgumentException("passwordHash cant be blank or null");
        }

        if (role == null) {
            throw new AuthArgumentException("role cant be null");
        }

        if (enabled == null || locked == null) {
            throw new AuthArgumentException("'enable' status or 'locked' status cant be null");
        }

        if (createdAt == null || lastLoginAt == null) {
            throw new AuthArgumentException("timestamps cant be null");
        }

        UUID uuid = UUID.randomUUID();
        return new AuthDomain(uuid, email, passwordHash, role, enabled, locked, createdAt, lastLoginAt);
    }

    public void updateLastLoginAt(LocalDateTime newLastLoginAt) {
        if (newLastLoginAt == null) {
            throw new AuthArgumentException("last login at can't be null");
        }
        if (this.lastLoginAt != null && newLastLoginAt.isBefore(this.lastLoginAt)) {
            throw new AuthArgumentException("last login at cant go backwards in time");
        }
        this.lastLoginAt = newLastLoginAt;
    }

    public void disable() {
        this.enabled = false;
    }

    public void lock() {
        this.locked = true;
    }

    public void enable() {
        this.enabled = true;
    }

    public void unlock() {
        this.locked = false;
    }
}
