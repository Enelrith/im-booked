package com.imbooked.shared.exception;

import com.imbooked.auth.InvalidTokenException;
import com.imbooked.user.exception.EmailAlreadyInUseException;
import com.imbooked.user.exception.PasswordsDoNotMatchException;
import com.imbooked.user.exception.RoleDoesNotExistException;
import com.imbooked.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

import static com.imbooked.shared.exception.ErrorMessage.buildErrorMessage;
import static com.imbooked.shared.exception.ErrorMessage.buildValidationErrorMessage;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e, HttpServletRequest request) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var response = buildErrorMessage(Instant.now(), status.value(), e.getMessage(), status.getReasonPhrase(), request.getServletPath());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var errors = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        var response = buildValidationErrorMessage(Instant.now(), status.value(), status.getReasonPhrase(), request.getServletPath(), errors);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseEntity<ErrorMessage> handleException(PasswordsDoNotMatchException e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var response = buildErrorMessage(Instant.now(), status.value(), e.getMessage(), status.getReasonPhrase(), request.getServletPath());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorMessage> handleException(EmailAlreadyInUseException e, HttpServletRequest request) {
        var status = HttpStatus.CONFLICT;
        var response = buildErrorMessage(Instant.now(), status.value(), e.getMessage(), status.getReasonPhrase(), request.getServletPath());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(RoleDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handleException(RoleDoesNotExistException e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var response = buildErrorMessage(Instant.now(), status.value(), e.getMessage(), status.getReasonPhrase(), request.getServletPath());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(UserNotFoundException e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var response = buildErrorMessage(Instant.now(), status.value(), e.getMessage(), status.getReasonPhrase(), request.getServletPath());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleException(BadCredentialsException e, HttpServletRequest request) {
        var status = HttpStatus.UNAUTHORIZED;
        var response = buildErrorMessage(Instant.now(), status.value(), e.getMessage(), status.getReasonPhrase(), request.getServletPath());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorMessage> handleException(InvalidTokenException e, HttpServletRequest request) {
        var status = HttpStatus.UNAUTHORIZED;
        var response = buildErrorMessage(Instant.now(), status.value(), e.getMessage(), status.getReasonPhrase(), request.getServletPath());

        return ResponseEntity.status(status).body(response);
    }
}
