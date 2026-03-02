package com.imbooked.appointment.exception;

import java.time.Instant;
import java.util.UUID;

public class ConflictingAppointmentTimeException extends RuntimeException {
    public ConflictingAppointmentTimeException(Instant newAppointmentStart, Instant newAppointmentEnd,
                                               UUID oldAppointmentId, Instant oldAppointmentStart, Instant oldAppointmentEnd) {
        super("Time for appointment (start: " + newAppointmentStart + ", end: " + newAppointmentEnd + ") " +
                "is conflicting with the time of appointment with id: " + oldAppointmentId +
                " (start: " + oldAppointmentStart + ", end: " + oldAppointmentEnd + ")");
    }

    public ConflictingAppointmentTimeException(String message) {
        super(message);
    }
}
