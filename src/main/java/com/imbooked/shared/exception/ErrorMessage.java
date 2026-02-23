package com.imbooked.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Getter
public class ErrorMessage {
    private Instant timestamp;
    private Integer status;
    private String message;
    private String error;
    private String path;
    private List<String> validationErrors;

    public static ErrorMessage buildErrorMessage(Instant timestamp, Integer status, String message, String error, String path) {
        log.warn("An exception has occurred for path {}, status {}, message: {}, error: {}", path, status, message, error);

        return new ErrorMessage(timestamp, status, message, error, path, null);
    }

    public static ErrorMessage buildValidationErrorMessage(Instant timestamp, Integer status, String error, String path, List<String> validationErrors) {
        log.warn("A validation error has occurred for path {}, status {}, error: {}, validation errors: {}", path, status, error, validationErrors);

        return new ErrorMessage(timestamp, status, "A validation error has occurred in one or more fields", error, path, validationErrors);
    }
}
