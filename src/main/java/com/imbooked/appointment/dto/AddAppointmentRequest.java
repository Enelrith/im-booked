package com.imbooked.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record AddAppointmentRequest(
        @Size(message = "The client name must be between {min} and {max} characters long", min = 1, max = 255)
        @NotBlank(message = "The client name cannot be empty")
        String clientName,
        @NotNull(message = "The appointment start time cannot be null")
        Instant appointmentStart,
        @NotNull(message = "The appointment end time cannot be null")
        Instant appointmentEnd,
        UUID serviceId) {
}