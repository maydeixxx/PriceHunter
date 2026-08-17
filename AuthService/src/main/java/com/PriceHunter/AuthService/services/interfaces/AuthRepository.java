package com.PriceHunter.AuthService.services.interfaces;

import com.PriceHunter.AuthService.models.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<AuthEntity, UUID> {
    Optional<AuthEntity> findAuthEntityByEmail(String email);
}
