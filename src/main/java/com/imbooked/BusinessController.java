package com.imbooked;

import com.imbooked.business.BusinessService;
import com.imbooked.business.dto.BusinessDto;
import com.imbooked.business.dto.BusinessThumbnailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
@Validated
public class BusinessController {
    private final BusinessService businessService;

    @GetMapping
    public ResponseEntity<Set<BusinessThumbnailDto>> getAllBusinessThumbnails() {

        return ResponseEntity.ok(businessService.getAllBusinessThumbnails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDto> getBusinessById(@PathVariable UUID id) {

        return ResponseEntity.ok(businessService.getBusinessById(id));
    }
}
