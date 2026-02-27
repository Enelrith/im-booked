package com.imbooked.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "businesses"})
    @Query("select u from User u where u.email = :email")
    Optional<User> findUserByEmailWithBusinesses(String email);
}