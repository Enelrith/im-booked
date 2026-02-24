package com.imbooked.auth;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Invalid JWT token");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
