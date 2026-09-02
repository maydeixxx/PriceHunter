package com.PriceHunter.AuthService.models.exceptions;

public class AuthExistsException extends RuntimeException {
    public AuthExistsException(String message) {
        super(message);
    }
}
