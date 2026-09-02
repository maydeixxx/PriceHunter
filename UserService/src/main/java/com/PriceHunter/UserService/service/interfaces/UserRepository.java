package com.PriceHunter.UserService.service.interfaces;

import com.PriceHunter.UserService.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
