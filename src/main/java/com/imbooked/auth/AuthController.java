package com.imbooked.auth;

import com.imbooked.auth.dto.JwtResponse;
import com.imbooked.auth.dto.LoginRequest;
import com.imbooked.auth.dto.LoginResponse;
import com.imbooked.user.dto.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        var jwtTokensResponse = authService.loginUser(request);
        var cookie = authService.buildCookie(jwtTokensResponse.refreshToken());
        response.addCookie(cookie);

        var loginResponse = new LoginResponse(jwtTokensResponse.email(), jwtTokensResponse.accessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@CookieValue(value = "refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(authService.me());
    }
}
