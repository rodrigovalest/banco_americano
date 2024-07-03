package com.rodrigovalest.ms_payment.exceptions;

public class RabbitMqMessagingException extends RuntimeException {
    public RabbitMqMessagingException(String message) {
        super(message);
    }
}
