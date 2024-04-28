package com.shop.cartinventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(value = {NoRecordFoundException.class})
    public ResponseEntity<?> handleNoRecordFoundException(NoRecordFoundException ex) {
        return new ResponseEntity<>(new ErrorData(ex.getMessage(), ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ClientException.class})
    public ResponseEntity<?> handleClientException(ClientException ex) {
        return new ResponseEntity<>(new ErrorData(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
