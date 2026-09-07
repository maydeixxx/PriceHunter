package com.PriceHunter.UserService.models.exceptions;

public class UserArgumentException extends RuntimeException {
    public UserArgumentException(String message) {
        super(message);
    }
}
