package com.rodrigovalest.ms_payment.web.exceptions;

import com.rodrigovalest.ms_payment.exceptions.CategoryNotFoundException;
import com.rodrigovalest.ms_payment.exceptions.CustomerNotFoundException;
import com.rodrigovalest.ms_payment.exceptions.MicroserviceConnectionErrorException;
import com.rodrigovalest.ms_payment.exceptions.RabbitMqMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<RestErrorMessage> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e,
            BindingResult bindingResult
    ) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, "invalid fields", bindingResult);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    private ResponseEntity<RestErrorMessage> customerNotFoundExceptionHandler(CustomerNotFoundException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(
                HttpStatus.NOT_FOUND, e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    private ResponseEntity<RestErrorMessage> categoryNotFoundExceptionHandler(CategoryNotFoundException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(
                HttpStatus.NOT_FOUND, e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(MicroserviceConnectionErrorException.class)
    private ResponseEntity<RestErrorMessage> microserviceConnectionErrorExceptionHandler(MicroserviceConnectionErrorException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(RabbitMqMessagingException.class)
    private ResponseEntity<RestErrorMessage> rabbitMqMessagingExceptionHandler(RabbitMqMessagingException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }
}