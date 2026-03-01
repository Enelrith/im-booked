package com.imbooked.business;

import com.imbooked.business.dto.AddBusinessRequest;
import com.imbooked.business.dto.BusinessDto;
import com.imbooked.business.dto.BusinessThumbnailDto;
import com.imbooked.service.ServiceMapper;
import com.imbooked.user.UserMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, ServiceMapper.class})
public interface BusinessMapper {
    Business toEntity(AddBusinessRequest addBusinessRequest);

    AddBusinessRequest toAddBusinessDto(Business business);

    Business toEntity(BusinessDto businessDto);

    BusinessDto toBusinessDto(Business business);

    Business toEntity(BusinessThumbnailDto businessThumbnailDto);

    BusinessThumbnailDto toBusinessThumbnailDto(Business business);
}