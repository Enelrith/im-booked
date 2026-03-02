package com.imbooked.appointment.exception;

public class InvalidAppointmentTimeException extends RuntimeException {
    public InvalidAppointmentTimeException() {
        super("Appointment times must both be valid or both be null");
    }

    public InvalidAppointmentTimeException(String message) {
        super(message);
    }
}
