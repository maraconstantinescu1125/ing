package com.example.testING.exception;

public class InvalidProductException extends  RuntimeException{
    public InvalidProductException(String message) {
        super(message);
    }
}
