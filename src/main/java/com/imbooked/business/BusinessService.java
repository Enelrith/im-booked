package com.imbooked.business;

import com.imbooked.business.dto.BusinessDto;
import com.imbooked.business.dto.BusinessThumbnailDto;
import com.imbooked.business.exception.BusinessNotFoundException;
import com.imbooked.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    public Set<BusinessThumbnailDto> getAllBusinessThumbnails() {
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var businesses = businessRepository.findAllByUser_Email(userEmail);

        log.info("Loaded businesses for user with email: {}", userEmail);
        return businesses.stream().map(businessMapper::toBusinessThumbnailDto).collect(Collectors.toSet());
    }

    public BusinessDto getBusinessById(UUID id) {
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var business = businessRepository.findByIdAndUser_Email(id, userEmail).orElseThrow(() -> new BusinessNotFoundException(id));

        log.info("Loaded business with id: {} for user with email: {}", id, userEmail);
        return businessMapper.toBusinessDto(business);
    }
}
