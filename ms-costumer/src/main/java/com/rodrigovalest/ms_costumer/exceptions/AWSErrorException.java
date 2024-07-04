package com.rodrigovalest.ms_costumer.exceptions;

public class AWSErrorException extends RuntimeException {
    public AWSErrorException(String message) {
        super(message);
    }
}
