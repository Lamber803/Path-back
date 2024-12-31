package com.example.demo.exception;


public class FlashcardGroupNotFoundException extends RuntimeException {

    public FlashcardGroupNotFoundException(String message) {
        super(message);  // Call to the parent (RuntimeException) constructor
    }

    public FlashcardGroupNotFoundException(String message, Throwable cause) {
        super(message, cause);  // Call to the parent constructor with cause
    }
}