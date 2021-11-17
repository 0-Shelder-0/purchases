package com.example.purchases.exceptions;

public class ValidationException extends Exception {
    public ValidationException(Throwable throwable) {
        super(throwable);
    }

    public ValidationException(String message) {
        super(message);
    }
}