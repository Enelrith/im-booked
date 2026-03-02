package com.imbooked.appointment.dto;

import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record UpdateAppointmentRequest(@Size(message = "The client name must be between {min} and {max} characters long", min = 1, max = 255)
                                       String clientName,
                                       Instant appointmentStart,
                                       Instant appointmentEnd,
                                       UUID serviceId) {
}
