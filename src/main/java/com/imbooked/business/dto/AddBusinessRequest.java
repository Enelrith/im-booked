package com.imbooked.business.dto;

import jakarta.validation.constraints.*;

public record AddBusinessRequest(
        @Size(message = "The business name must be between {min} and {max} characters long", min = 1, max = 255)
        @NotBlank(message = "The business name cannot be empty")
        String name,
        @Size(message = "The business email must be between {min} and {max} characters long", min = 1, max = 255)
        @Email(message = "Must be a valid email") @NotBlank(message = "The business email cannot be empty")
        String email,
        @Size(message = "The phone number must be between {min} and {max} characters long", min = 8, max = 20)
        @Pattern(message = "Must be a valid phone number", regexp = "^\\+?[0-9]+$")
        String phone,
        @Size(message = "The description must be between {min} and {max} characters long", min = 1, max = 255)
        String description,
        @Size(message = "The address must be between {min} and {max} characters long", min = 1, max = 255)
        String address,
        @Size(message = "The country must be between {min} and {max} characters long", min = 1, max = 255)
        String country,
        @NotNull(message = "isActive cannot be null")
        Boolean isActive) {
}