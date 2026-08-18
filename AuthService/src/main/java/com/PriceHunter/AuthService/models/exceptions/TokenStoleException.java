package com.PriceHunter.AuthService.models.exceptions;

public class TokenStoleException extends RuntimeException {
    public TokenStoleException(String message) {
        super(message);
    }
}
