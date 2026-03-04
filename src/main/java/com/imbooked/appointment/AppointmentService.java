package com.imbooked.appointment;

import com.imbooked.appointment.dto.AddAppointmentRequest;
import com.imbooked.appointment.dto.AppointmentDto;
import com.imbooked.appointment.dto.UpdateAppointmentRequest;
import com.imbooked.appointment.exception.AppointmentNotFoundException;
import com.imbooked.appointment.exception.ConflictingAppointmentTimeException;
import com.imbooked.appointment.exception.InvalidAppointmentTimeException;
import com.imbooked.business.Business;
import com.imbooked.business.BusinessRepository;
import com.imbooked.business.exception.BusinessNotFoundException;
import com.imbooked.service.ServiceRepository;
import com.imbooked.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final BusinessRepository businessRepository;
    private final AppointmentMapper appointmentMapper;
    private final ServiceRepository serviceRepository;

    @Transactional
    public AppointmentDto addAppointment(UUID businessId, AddAppointmentRequest request) {
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var business = businessRepository.findByIdAndUser_Email(businessId, userEmail)
                .orElseThrow(() -> new BusinessNotFoundException(businessId));
        var service = serviceRepository.findByIdAndBusiness_User_Email(request.serviceId(), userEmail)
                .orElse(null);
        var appointments = appointmentRepository.findAllByStatusAndBusiness_IdAndBusiness_User_Email(
                AppointmentStatus.SCHEDULED, businessId, userEmail);

        checkAppointmentTime(appointments, request.appointmentStart(), request.appointmentEnd());

        var appointment = appointmentMapper.toEntity(request);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        addAppointmentToBusinessAndService(business, service, appointment);

        appointmentRepository.flush();
        log.info("Appointment with id: {} created for user: {}", appointment.getId(), userEmail);

        return appointmentMapper.toAppointmentDto(appointment);
    }

    @Transactional
    public AppointmentDto updateAppointment(UUID businessId, UUID appointmentId, UpdateAppointmentRequest request) {
        if ((request.appointmentStart() == null && request.appointmentEnd() != null) ||
                (request.appointmentStart() != null && request.appointmentEnd() == null)) {
            throw new InvalidAppointmentTimeException();
        }
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var service = serviceRepository.findByIdAndBusiness_User_Email(request.serviceId(), userEmail)
                .orElse(null);
        var appointments = appointmentRepository.findAllByStatusAndBusiness_IdAndBusiness_User_Email(
                AppointmentStatus.SCHEDULED, businessId, userEmail);
        var appointmentToUpdate = appointments.stream()
                .filter(appointment -> appointment.getId().equals(appointmentId)).findFirst()
                .orElseThrow(()  -> new AppointmentNotFoundException(appointmentId));
        var otherAppointments = appointments.stream()
                .filter(appointment -> !appointment.getId().equals(appointmentId)).collect(Collectors.toSet());

        if (request.appointmentStart() != null)
            checkAppointmentTime(otherAppointments, request.appointmentStart(), request.appointmentEnd());

        var updatedAppointment = appointmentMapper.partialUpdate(request, appointmentToUpdate);
        if (service != null) updatedAppointment.setService(service);

        appointmentRepository.flush();
        log.info("Updated appointment with id: {}", appointmentId);

        return appointmentMapper.toAppointmentDto(updatedAppointment);
    }

    @Transactional
    public void deleteAppointment(UUID appointmentId) {
        var appointment = appointmentRepository.findByIdAndBusiness_User_Email(appointmentId, SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
        appointmentRepository.delete(appointment);
        log.info("Deleted appointment with id: {}", appointmentId);
    }

    public Page<AppointmentDto> getAppointments(UUID businessId, Pageable pageable) {
        var appointments = appointmentRepository.findAllByBusinessIdAndBusiness_User_Email(businessId,
                SecurityUtils.getCurrentUserEmail(), pageable);
        return appointments.map(appointmentMapper::toAppointmentDto);
    }

    private void addAppointmentToBusinessAndService(Business business, com.imbooked.service.Service service,
                                                    Appointment appointment) {
        business.getAppointments().add(appointment);
        appointment.setBusiness(business);
        if (service != null) {
            service.getAppointments().add(appointment);
            appointment.setService(service);
        }
    }

    private void checkAppointmentTime(Set<Appointment> appointments, Instant appointmentStart, Instant appointmentEnd) {
        appointments.forEach(appointment -> {
            if (appointmentStart.isBefore(appointment.getAppointmentEnd())
                    && appointmentEnd.isAfter(appointment.getAppointmentStart())) {

                throw new ConflictingAppointmentTimeException(appointmentStart, appointmentEnd,
                        appointment.getId(), appointment.getAppointmentStart(), appointment.getAppointmentEnd());
            }
        });
    }
}
