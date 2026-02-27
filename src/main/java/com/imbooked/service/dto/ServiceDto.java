package com.imbooked.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceDto(UUID id, String name, BigDecimal price, Integer durationMinutes, Boolean isActive,
                         UUID businessId) {
}