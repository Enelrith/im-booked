package com.imbooked.user;

import com.imbooked.shared.enums.RoleName;
import com.imbooked.user.dto.UserRequest;
import com.imbooked.user.dto.UserResponse;
import com.imbooked.user.exception.EmailAlreadyInUseException;
import com.imbooked.user.exception.PasswordsDoNotMatchException;
import com.imbooked.user.exception.RoleDoesNotExistException;
import com.imbooked.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(UserRequest request) {
        if (!request.password().equals(request.rePassword())) throw new PasswordsDoNotMatchException();
        if (userRepository.existsByEmail(request.email())) throw new EmailAlreadyInUseException();

        var user = userMapper.toEntity(request);
        addUserRole(user, RoleName.USER);

        var hashedPassword = passwordEncoder.encode(request.password());
        user.setPassword(hashedPassword);

        var savedUser = userRepository.saveAndFlush(user);
        log.info("User with id: {} has been registered", user.getId());

        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse getUserById(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toUserResponse(user);
    }

    private void addUserRole(User user, RoleName roleName) {
        var role = roleRepository.findByName(roleName).orElseThrow(() -> new RoleDoesNotExistException(roleName));

        user.getRoles().add(role);
    }
}
