package com.example.demo.exception;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);  // 透過構造函數將消息傳遞給父類（RuntimeException）
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);  // 支援傳遞異常原因
    }
}
