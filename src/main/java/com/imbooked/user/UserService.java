package com.imbooked.user;

import com.imbooked.business.BusinessMapper;
import com.imbooked.business.dto.AddBusinessRequest;
import com.imbooked.business.dto.BusinessDto;
import com.imbooked.business.exception.BusinessEmailAlreadyInUseException;
import com.imbooked.shared.SecurityUtils;
import com.imbooked.shared.config.PasswordEncoderConfig;
import com.imbooked.shared.enums.RoleName;
import com.imbooked.user.dto.UserRequest;
import com.imbooked.user.dto.UserResponse;
import com.imbooked.user.exception.EmailAlreadyInUseException;
import com.imbooked.user.exception.PasswordsDoNotMatchException;
import com.imbooked.user.exception.RoleDoesNotExistException;
import com.imbooked.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BusinessMapper businessMapper;
    private final PasswordEncoderConfig passwordEncoderConfig;

    @Transactional
    public UserResponse registerUser(UserRequest request) {
        if (!request.password().equals(request.rePassword())) throw new PasswordsDoNotMatchException();
        if (userRepository.existsByEmail(request.email())) throw new EmailAlreadyInUseException();

        var user = userMapper.toEntity(request);
        addUserRole(user, RoleName.USER);

        var hashedPassword = passwordEncoderConfig.passwordEncoder().encode(request.password());
        user.setPassword(hashedPassword);

        var savedUser = userRepository.saveAndFlush(user);
        log.info("User with id: {} has been registered", user.getId());

        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse getUserById(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public BusinessDto addBusiness(AddBusinessRequest request) {
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var user = userRepository.findUserByEmailWithBusinesses(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));

        user.getBusinesses().forEach(business -> {
            if (business.getEmail().equals(request.email())) {
                throw new BusinessEmailAlreadyInUseException();
            }
        });

        var business = businessMapper.toEntity(request);
        user.addBusiness(business);
        log.info("User with email: {} created business: {}", userEmail, business.getName());

        var roleNames = user.getRoles().stream().map(Role::getName).toList();
        if (!roleNames.contains(RoleName.BUSINESS_OWNER)) addUserRole(user, RoleName.BUSINESS_OWNER);

        userRepository.flush();

        return businessMapper.toBusinessDto(business);
    }

    private void addUserRole(User user, RoleName roleName) {
        var role = roleRepository.findByName(roleName).orElseThrow(() -> new RoleDoesNotExistException(roleName));

        user.getRoles().add(role);
        log.info("Role: {} has been added to user with email: {}", roleName, user.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
