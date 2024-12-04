package com.example.demo.exception;

public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(String message) {
        super(message);  // Call to the parent (RuntimeException) constructor
    }

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);  // Call to the parent constructor with cause
    }
}

