package com.imbooked.health.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HealthDto(@NotBlank(message = "Health message must not be blank")
                        @Size(min = 1, max = 20, message = "Health message must be between {min} and {max} characters long")
                        String message) {
}
