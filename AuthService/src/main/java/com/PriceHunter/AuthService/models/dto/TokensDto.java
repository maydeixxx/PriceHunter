package com.PriceHunter.AuthService.models.dto;

import lombok.Builder;

@Builder
public class TokensDto {
    private String accessToken;
    private String refreshToken;
}
