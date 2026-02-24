package com.imbooked.auth;

import com.imbooked.shared.enums.RoleName;
import com.imbooked.user.Role;
import com.imbooked.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;
    @Value("${spring.jwt.access-token-expiration}")
    private int accessTokenExpiration;
    @Value("${spring.jwt.refresh-token-expiration}")
    @Getter
    private int refreshTokenExpiration;

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration);
    }

    private String generateToken(User user, long tokenExpiration) {
        var roles = user.getRoles().stream().map(Role::getName).toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);

            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            var claims = getClaims(token);

            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<RoleName> getRolesFromToken(String token) {
        try {
            var claims = getClaims(token);
            var roles = (List<String>) claims.get("roles");
            return roles.stream().map(RoleName::valueOf).toList();
        } catch (JwtException e) {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
