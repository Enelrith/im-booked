package com.imbooked.service.exception;

import java.util.UUID;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(UUID serviceId) {
        super("Service with ID " + serviceId + " not found");
    }

    public ServiceNotFoundException(String message) {
        super(message);
    }
}
