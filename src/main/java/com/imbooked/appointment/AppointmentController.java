package com.imbooked.appointment;

import com.imbooked.appointment.dto.AddAppointmentRequest;
import com.imbooked.appointment.dto.AppointmentDto;
import com.imbooked.appointment.dto.UpdateAppointmentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Validated
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/{businessId}")
    public ResponseEntity<AppointmentDto> addAppointment(@PathVariable UUID businessId, @RequestBody @Valid AddAppointmentRequest request) {
        var appointment = appointmentService.addAppointment(businessId, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(appointment.id())
                .toUri();
        return ResponseEntity.created(location).body(appointment);
    }

    @PatchMapping("/{businessId}/{appointmentId}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable UUID businessId, @PathVariable UUID appointmentId,
                                                            @RequestBody @Valid UpdateAppointmentRequest request) {

        return ResponseEntity.ok(appointmentService.updateAppointment(businessId, appointmentId, request));
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID appointmentId) {
        appointmentService.deleteAppointment(appointmentId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<Page<AppointmentDto>> getAppointments(@PathVariable UUID businessId,
                                                                @RequestParam(defaultValue = "0") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("status").descending()
                .and(Sort.by("appointmentStart").ascending()));
        return ResponseEntity.ok(appointmentService.getAppointments(businessId, pageable));
    }
}
