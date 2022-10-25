package com.epam.training.fooddelivery.service;

import org.springframework.stereotype.Component;


public class LowBalanceException extends RuntimeException {
    public LowBalanceException(String message) {
        super(message);
    }
}
