package com.imbooked.auth.dto;

public record JwtTokensResponse(String email, String accessToken, String refreshToken) {
}
