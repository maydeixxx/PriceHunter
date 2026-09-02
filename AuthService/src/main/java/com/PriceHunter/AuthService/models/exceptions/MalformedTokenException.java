package com.PriceHunter.AuthService.models.exceptions;

public class MalformedTokenException extends RuntimeException {
    public MalformedTokenException(String message) {
        super(message);
    }
}
