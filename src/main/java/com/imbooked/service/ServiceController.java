package com.imbooked.service;

import com.imbooked.service.dto.AddServiceRequest;
import com.imbooked.service.dto.ServiceDto;
import com.imbooked.service.dto.UpdateServiceRequest;
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

    @PatchMapping("/{serviceId}")
    public ResponseEntity<ServiceDto> updateService(@PathVariable UUID serviceId, @RequestBody @Valid UpdateServiceRequest request) {

        return ResponseEntity.ok(serviceService.updateService(serviceId, request));
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID serviceId) {
        serviceService.deleteService(serviceId);

        return ResponseEntity.noContent().build();
    }
}
