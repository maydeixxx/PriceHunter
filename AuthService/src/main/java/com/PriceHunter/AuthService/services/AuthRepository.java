package com.PriceHunter.AuthService.services;

import com.PriceHunter.AuthService.models.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthRepository extends JpaRepository<AuthEntity, UUID> {
}
