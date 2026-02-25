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
        var cookie = buildCookie(jwtTokensResponse.refreshToken(), jwtService.getRefreshTokenExpiration());
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        authService.logoutUser(refreshToken);

        var cookie = buildCookie(null, 0);
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    private Cookie buildCookie(String refreshToken, int expiration) {
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(expiration);
        cookie.setSecure(false); //TODO: Change this to true after deployment

        return cookie;
    }
}
