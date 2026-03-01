package com.imbooked.service;

import com.imbooked.business.Business;
import com.imbooked.business.BusinessRepository;
import com.imbooked.business.exception.BusinessNotFoundException;
import com.imbooked.service.dto.AddServiceRequest;
import com.imbooked.service.dto.ServiceDto;
import com.imbooked.service.dto.UpdateServiceRequest;
import com.imbooked.service.exception.ServiceNotFoundException;
import com.imbooked.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Slf4j
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final BusinessRepository businessRepository;

    @Transactional
    public ServiceDto addService(UUID businessId, AddServiceRequest request) {
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var business = businessRepository.findByIdAndUser_Email(businessId, userEmail)
                .orElseThrow(() -> new BusinessNotFoundException(businessId));
        var service = serviceMapper.toEntity(request);

        addServiceToBusiness(service, business);

        serviceRepository.flush();

        return serviceMapper.toServiceDto(service);
    }

    @Transactional
    public ServiceDto updateService(UUID serviceId, UpdateServiceRequest request) {
        var service = serviceRepository.findByIdAndBusiness_User_Email(serviceId, SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));

        serviceMapper.updateService(request, service);
        serviceRepository.flush();

        return serviceMapper.toServiceDto(service);
    }

    @Transactional
    public void deleteService(UUID serviceId) {
        var service = serviceRepository.findByIdAndBusiness_User_Email(serviceId, SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));
        serviceRepository.delete(service);
    }

    public Set<ServiceDto> getServicesByBusinessId(UUID businessId) {
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var business = businessRepository.findByIdAndUser_Email(businessId, userEmail)
                .orElseThrow(() -> new BusinessNotFoundException(businessId));
        var services = business.getServices();

        return services.stream().map(serviceMapper::toServiceDto).collect(Collectors.toSet());
    }

    private void addServiceToBusiness(Service service, Business business) {
        business.getServices().add(service);
        service.setBusiness(business);
    }
}
