package com.example.demo.exception;


public class DocumentGroupNotFoundException extends RuntimeException {

    public DocumentGroupNotFoundException(String message) {
        super(message);  // Call to the parent (RuntimeException) constructor
    }

    public DocumentGroupNotFoundException(String message, Throwable cause) {
        super(message, cause);  // Call to the parent constructor with cause
    }
}