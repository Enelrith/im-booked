package com.imbooked.service;

import com.imbooked.service.dto.AddServiceRequest;
import com.imbooked.service.dto.ServiceDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Validated
public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping("/{businessId}")
    public ResponseEntity<ServiceDto> addService(@PathVariable UUID businessId, @Valid @RequestBody AddServiceRequest request) {
        var serviceDto = serviceService.addService(businessId, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(serviceDto.id())
                .toUri();
        return ResponseEntity.created(location).body(serviceDto);
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<Set<ServiceDto>> getServicesByBusinessId(@PathVariable UUID businessId) {

        return ResponseEntity.ok(serviceService.getServicesByBusinessId(businessId));
    }
}
