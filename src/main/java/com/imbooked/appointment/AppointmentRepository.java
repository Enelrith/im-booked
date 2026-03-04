package com.imbooked.appointment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Set<Appointment> findAllByStatusAndBusiness_IdAndBusiness_User_Email(AppointmentStatus status, UUID businessId, String userEmail);

    Optional<Appointment> findByIdAndBusiness_User_Email(UUID id, String userEmail);

    Page<Appointment> findAllByBusinessIdAndBusiness_User_Email(UUID businessId, String userEmail, Pageable pageable);
}