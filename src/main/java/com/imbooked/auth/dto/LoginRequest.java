package com.imbooked.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Size(message = "The email must be between {min} and {max} characters long", min = 1, max = 255)
        @Email(message = "Must be a valid email")
        @NotBlank(message = "The email must not be empty")
        String email,
        @Size(message = "The password must be between {min} and {max} characters long", min = 8, max = 72)
        @NotBlank(message = "The password must not be empty")
        String password) {
}