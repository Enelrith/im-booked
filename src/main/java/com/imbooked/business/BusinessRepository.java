package com.imbooked.business;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {
    Optional<Business> findByIdAndUser_Email(UUID id, String email);

    Set<Business> findAllByUser_Email(String userEmail);
}