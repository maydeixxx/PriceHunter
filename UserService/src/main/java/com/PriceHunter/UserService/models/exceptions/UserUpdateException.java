package com.PriceHunter.UserService.models.exceptions;

public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message) {
        super(message);
    }
}
