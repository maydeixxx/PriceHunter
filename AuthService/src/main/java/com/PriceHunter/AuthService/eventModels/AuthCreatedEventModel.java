package com.PriceHunter.AuthService.eventModels;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
public class AuthCreatedEventModel {
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}