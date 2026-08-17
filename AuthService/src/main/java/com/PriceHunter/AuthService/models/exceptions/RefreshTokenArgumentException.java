package com.PriceHunter.AuthService.models.exceptions;

public class RefreshTokenArgumentException extends RuntimeException {
    public RefreshTokenArgumentException(String message) {
        super(message);
    }
}
