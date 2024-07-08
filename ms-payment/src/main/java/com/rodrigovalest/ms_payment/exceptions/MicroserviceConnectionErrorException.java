package com.rodrigovalest.ms_payment.exceptions;

public class MicroserviceConnectionErrorException extends RuntimeException {
    public MicroserviceConnectionErrorException(String message) {
        super(message);
    }
}
