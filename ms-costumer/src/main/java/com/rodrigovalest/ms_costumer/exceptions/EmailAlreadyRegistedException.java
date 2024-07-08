package com.rodrigovalest.ms_costumer.exceptions;

public class EmailAlreadyRegistedException extends RuntimeException {
    public EmailAlreadyRegistedException(String message) {
        super(message);
    }
}
