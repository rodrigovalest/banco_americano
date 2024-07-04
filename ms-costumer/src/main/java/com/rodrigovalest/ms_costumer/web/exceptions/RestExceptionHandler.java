package com.rodrigovalest.ms_costumer.web.exceptions;

import com.rodrigovalest.ms_costumer.exceptions.*;
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

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<RestErrorMessage> runtimeExceptionHandler(RuntimeException e) {
        log.info(String.valueOf(e.getCause()) + " | " + e.getMessage());
        e.printStackTrace();

        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(CpfAlreadyRegisteredException.class)
    private ResponseEntity<RestErrorMessage> cpfAlreadyRegisteredExceptionHandler(CpfAlreadyRegisteredException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(EmailAlreadyRegistedException.class)
    private ResponseEntity<RestErrorMessage> emailAlreadyRegistedExceptionHandler(EmailAlreadyRegistedException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<RestErrorMessage> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(
                HttpStatus.NOT_FOUND, e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(InvalidCpfException.class)
    private ResponseEntity<RestErrorMessage> invalidCpfExceptionHandler(InvalidCpfException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(AWSErrorException.class)
    private ResponseEntity<RestErrorMessage> AWSErrorExceptionHandler(AWSErrorException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(FileConvertionException.class)
    private ResponseEntity<RestErrorMessage> fileConvertionExceptionHandler(FileConvertionException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(FileSizeException.class)
    private ResponseEntity<RestErrorMessage> fileSizeExceptionHandler(FileSizeException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }
}
