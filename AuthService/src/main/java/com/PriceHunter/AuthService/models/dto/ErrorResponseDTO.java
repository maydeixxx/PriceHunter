package com.PriceHunter.AuthService.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponseDTO {
    private String error;
    private String errorMessage;
    private int code;
    private String path;
}
