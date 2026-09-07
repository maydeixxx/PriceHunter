package com.PriceHunter.UserService.models.eventModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthCreatedEventModel {
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
