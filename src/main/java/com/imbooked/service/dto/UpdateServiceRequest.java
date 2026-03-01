package com.imbooked.service.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateServiceRequest(
        @Size(message = "The service name must be between {min} and {max} characters long", min = 1, max = 100)
        String name,
        @Digits(message = "The service price can have up to {integer} digits and {fraction} decimal places", integer = 8, fraction = 2)
        @PositiveOrZero(message = "The price must be either positive or zero")
        BigDecimal price,
        @Positive(message = "The duration must be a positive number")
        Integer durationMinutes,
        Boolean isActive) {
}