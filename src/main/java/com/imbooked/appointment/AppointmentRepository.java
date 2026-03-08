package com.imbooked.appointment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Set<Appointment> findAllByStatusAndBusiness_IdAndBusiness_User_Email(AppointmentStatus status, UUID businessId, String userEmail);

    Optional<Appointment> findByIdAndBusiness_User_Email(UUID id, String userEmail);

    Page<Appointment> findAllByBusinessIdAndBusiness_User_Email(UUID businessId, String userEmail, Pageable pageable);

    // --- REPORT QUERIES ---
    Long countAllByBusiness_IdAndBusiness_User_Email(UUID businessId, String userEmail);

    Long countAllByBusiness_IdAndBusiness_User_EmailAndAppointmentStartBetween(
            UUID businessId, String userEmail, Instant monthStart, Instant monthEnd
    );

    Long countAllByStatusAndBusiness_IdAndBusiness_User_Email(AppointmentStatus status, UUID businessId, String userEmail);

    Long countAllByStatusAndBusiness_IdAndBusiness_User_EmailAndAppointmentStartBetween(
            AppointmentStatus status, UUID businessId, String userEmail, Instant monthStart, Instant monthEnd
    );

    Long countAllByService_IdAndBusiness_IdAndBusiness_User_Email(UUID serviceId, UUID businessId, String userEmail);

    Long countAllByAppointmentStartBetweenAndBusiness_IdAndBusiness_User_Email(
            Instant startTime, Instant endTime, UUID businessId, String userEmail
    );

    @Query("select sum(a.service.price) from Appointment a " +
            "where a.status = :status " +
            "and a.business.id = :businessId " +
            "and a.business.user.email = :userEmail")
    Optional<BigDecimal> sumRevenueByStatus(
            AppointmentStatus status,
            UUID businessId,
            String userEmail
    );

    @Query("select sum(a.service.price) from Appointment a " +
            "where a.status = :status " +
            "and a.business.id = :businessId " +
            "and a.business.user.email = :userEmail " +
            "and a.appointmentStart >= :monthStart " +
            "and a.appointmentStart < :monthEnd")
    Optional<BigDecimal> sumMonthlyRevenueByStatus(
            AppointmentStatus status,
            UUID businessId,
            String userEmail,
            Instant monthStart,
            Instant monthEnd
    );
}