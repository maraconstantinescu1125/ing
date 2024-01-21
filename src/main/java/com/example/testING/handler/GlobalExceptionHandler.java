package com.example.testING.handler;

import com.example.testING.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, InvalidPriceException.class, InvalidProductException.class, InvalidQuantityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIntegrityConstraintViolation(RuntimeException ex) {
        String errorMessage = "Integrity constraint violation: " + ex.getMessage();

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler({CategoryNotFoundException.class, ProductNotFoundException.class, StockNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        String errorMessage = "Not Found Exception: " + ex.getMessage();

        return ResponseEntity.badRequest().body(errorMessage);
    }

}