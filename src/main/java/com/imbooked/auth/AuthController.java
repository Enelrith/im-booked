package com.imbooked.auth;

import com.imbooked.auth.dto.LoginRequest;
import com.imbooked.auth.dto.LoginResponse;
import com.imbooked.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest request) {

        return ResponseEntity.ok(authService.loginUser(request));
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String authHeader) {
        log.info("Validate called");
        var token = authHeader.replace("Bearer ", "");

        return jwtService.validateToken(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(authService.me());
    }
}
