package com.example.demo.exception;

public class UserNotFoundException extends CertException {
	
	public UserNotFoundException() {
		super("message");
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
	
}
