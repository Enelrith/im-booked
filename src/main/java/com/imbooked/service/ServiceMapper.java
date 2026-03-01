package com.imbooked.service;

import com.imbooked.service.dto.AddServiceRequest;
import com.imbooked.service.dto.ServiceDto;
import com.imbooked.service.dto.UpdateServiceRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceMapper {
    Service toEntity(AddServiceRequest addServiceRequest);

    @Mapping(source = "business.id", target = "businessId")
    ServiceDto toServiceDto(Service service);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Service updateService(UpdateServiceRequest request, @MappingTarget Service service);

    Service toEntity(UpdateServiceRequest updateServiceRequest);

    UpdateServiceRequest toDto(Service service);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Service partialUpdate(UpdateServiceRequest updateServiceRequest, @MappingTarget Service service);
}