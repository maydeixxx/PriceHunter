package com.PriceHunter.AuthService.models.dto;

import lombok.*;

@Value
@Builder
public class TokensDTO {
    String accessToken;
    String refreshToken;
}
