package com.imbooked.service;

import com.imbooked.service.dto.AddServiceRequest;
import com.imbooked.service.dto.ServiceDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceMapper {
    Service toEntity(AddServiceRequest addServiceRequest);

    @Mapping(source = "business.id", target = "businessId")
    ServiceDto toServiceDto(Service service);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "businessId", target = "business.id")
    Service partialUpdate(ServiceDto serviceDto, @MappingTarget Service service);
}