package com.imbooked.health;

import com.imbooked.health.dto.HealthDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Validated
public class HealthController {

    @PostMapping
    public ResponseEntity<HealthDto> postHealth(@RequestBody @Valid HealthDto healthDto) {
        return ResponseEntity.ok(healthDto);
    }
}
