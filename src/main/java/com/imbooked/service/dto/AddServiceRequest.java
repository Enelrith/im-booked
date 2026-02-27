package com.imbooked.service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AddServiceRequest(
        @Size(message = "The service name must be between {min} and {max} characters long", min = 1, max = 100)
        @NotBlank(message = "The service name cannot be empty")
        String name,
        @NotNull(message = "The service price cannot be null")
        @Digits(message = "The price can contain up to {integer} digits and {fraction} decimal places", integer = 8, fraction = 2)
        @PositiveOrZero(message = "The service price must be either positive or zero")
        BigDecimal price,
        @NotNull(message = "The duration cannot be empty")
        @Positive(message = "The duration must be a positive number")
        Integer durationMinutes,
        @NotNull(message = "Is Active cannot be null")
        Boolean isActive) {
}