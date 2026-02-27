package com.imbooked.user;

import com.imbooked.business.dto.AddBusinessRequest;
import com.imbooked.business.dto.BusinessDto;
import com.imbooked.user.dto.UserRequest;
import com.imbooked.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest request) {
        var user = userService.registerUser(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        var user = userService.getUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/businesses")
    public ResponseEntity<BusinessDto> addBusiness(@RequestBody @Valid AddBusinessRequest request) {
        var business = userService.addBusiness(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(business.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
