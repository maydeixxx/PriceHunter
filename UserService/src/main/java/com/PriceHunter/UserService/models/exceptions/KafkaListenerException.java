package com.PriceHunter.UserService.models.exceptions;

public class KafkaListenerException extends RuntimeException {
    public KafkaListenerException(String message) {
        super(message);
    }
}
