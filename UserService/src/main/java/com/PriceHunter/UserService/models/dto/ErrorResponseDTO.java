package com.PriceHunter.UserService.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponseDTO {
    private final String error;
    private final String errorMessage;
    private final int code;
    private final String path;
}
