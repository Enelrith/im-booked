package com.imbooked.auth;

import com.imbooked.auth.dto.JwtResponse;
import com.imbooked.auth.dto.JwtTokensResponse;
import com.imbooked.auth.dto.LoginRequest;
import com.imbooked.user.UserMapper;
import com.imbooked.user.UserRepository;
import com.imbooked.user.dto.UserDto;
import com.imbooked.user.exception.UserNotFoundException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public JwtTokensResponse loginUser(LoginRequest request) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        log.info("User with email: {} logged in", request.email());

        var user = userRepository.findUserByEmail(request.email()).orElseThrow(() -> new UserNotFoundException(request.email()));
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new JwtTokensResponse(request.email(), accessToken, refreshToken);
    }

    public UserDto me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = (String) authentication.getPrincipal();

        var user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        return userMapper.toUserDto(user);
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) throw new InvalidTokenException("Invalid refresh JWT token");

        var email = jwtService.getEmailFromToken(refreshToken);
        var user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        var accessToken = jwtService.generateAccessToken(user);
        log.info("User with email: {} refreshed their access token", email);

        return new JwtResponse(accessToken);
    }

    public Cookie buildCookie(String refreshToken) {
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(jwtService.getRefreshTokenExpiration());
        cookie.setSecure(true);

        return cookie;
    }
}
