package com.imbooked.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
    Optional<Service> findByIdAndBusiness_User_Email(UUID serviceId, String email);

    boolean existsByIdAndBusiness_User_Email(UUID serviceId, String email);
}