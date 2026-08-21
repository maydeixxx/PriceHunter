package com.PriceHunter.AuthService.models.exceptions;

public class AuthNotFoundException extends RuntimeException {
    public AuthNotFoundException(String message) {
        super(message);
    }
}
