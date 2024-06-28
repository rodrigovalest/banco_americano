package com.rodrigovalest.ms_calculate.web.exceptions;

import com.rodrigovalest.ms_calculate.exceptions.EntityNotFoundException;
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

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<RestErrorMessage> entityNotFoundExceptionnHandler(EntityNotFoundException e) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(restErrorMessage);
    }
}
