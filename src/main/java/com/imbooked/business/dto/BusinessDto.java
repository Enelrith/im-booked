package com.imbooked.business.dto;

import com.imbooked.user.dto.UserDto;

import java.time.Instant;
import java.util.UUID;

public record BusinessDto(UUID id, String name, String email, String phone, String description, String address,
                          String city, String country, Boolean isActive, Instant createdAt, Instant updatedAt,
                          UserDto user) {
}