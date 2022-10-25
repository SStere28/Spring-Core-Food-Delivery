package com.epam.training.fooddelivery.service;

import org.springframework.stereotype.Component;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
