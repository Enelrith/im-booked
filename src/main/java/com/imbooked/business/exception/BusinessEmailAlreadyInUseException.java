package com.imbooked.business.exception;

public class BusinessEmailAlreadyInUseException extends RuntimeException {
    public BusinessEmailAlreadyInUseException() {
        super("This business email is already in use");
    }

    public BusinessEmailAlreadyInUseException(String message) {
        super(message);
    }
}
