package com.imbooked.appointment.dto;

import com.imbooked.appointment.AppointmentStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for {@link com.imbooked.appointment.Appointment}
 */
public record AppointmentDto(UUID id, String clientName, AppointmentStatus status, Instant appointmentStart,
                             Instant appointmentEnd, Instant createdAt, Instant updatedAt, String serviceName,
                             BigDecimal servicePrice, Integer serviceDurationMinutes) {
}