package com.PriceHunter.AuthService.models.exceptions;

public class AuthArgumentException extends RuntimeException {
    public AuthArgumentException(String message) {
        super(message);
    }
}
