package com.imbooked.appointment;

import com.imbooked.appointment.dto.AddAppointmentRequest;
import com.imbooked.appointment.dto.AppointmentDto;
import com.imbooked.appointment.dto.UpdateAppointmentRequest;
import com.imbooked.appointment.dto.UpdateAppointmentStatusRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
    Appointment toEntity(AddAppointmentRequest addAppointmentRequest);

    @Mapping(source = "serviceDurationMinutes", target = "service.durationMinutes")
    @Mapping(source = "servicePrice", target = "service.price")
    @Mapping(source = "serviceName", target = "service.name")
    Appointment toEntity(AppointmentDto appointmentDto);

    @InheritInverseConfiguration(name = "toEntity")
    AppointmentDto toAppointmentDto(Appointment appointment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdate(UpdateAppointmentRequest updateAppointmentRequest, @MappingTarget Appointment appointment);

    Appointment toEntity(UpdateAppointmentStatusRequest updateAppointmentStatusRequest);

    UpdateAppointmentStatusRequest toUpdateAppointmentStatusDto(Appointment appointment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdate(UpdateAppointmentStatusRequest updateAppointmentStatusRequest, @MappingTarget Appointment appointment);
}