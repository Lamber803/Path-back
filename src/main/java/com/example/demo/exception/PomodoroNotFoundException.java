package com.example.demo.exception;

public class PomodoroNotFoundException extends RuntimeException {
    public PomodoroNotFoundException(String message) {
        super(message);
    }
}