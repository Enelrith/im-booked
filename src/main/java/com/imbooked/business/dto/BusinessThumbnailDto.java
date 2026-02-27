package com.imbooked.business.dto;

import java.util.UUID;

public record BusinessThumbnailDto(UUID id, String name, String description, Boolean isActive) {
}