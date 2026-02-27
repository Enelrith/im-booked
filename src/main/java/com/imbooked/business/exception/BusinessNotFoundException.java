package com.imbooked.business.exception;

import java.util.UUID;

public class BusinessNotFoundException extends RuntimeException {
    public BusinessNotFoundException(UUID id) {
        super("No business found with id: " + id);
    }

    public BusinessNotFoundException(String message) {
        super(message);
    }
}
