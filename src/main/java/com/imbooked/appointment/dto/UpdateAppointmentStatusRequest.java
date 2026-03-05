package com.imbooked.appointment.dto;

import com.imbooked.appointment.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentStatusRequest(
        @NotNull(message = "The appointment status cannot be null") AppointmentStatus status) {
}