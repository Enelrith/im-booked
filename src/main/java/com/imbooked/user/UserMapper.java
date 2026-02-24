package com.imbooked.user;

import com.imbooked.user.dto.UserDto;
import com.imbooked.user.dto.UserRequest;
import com.imbooked.user.dto.UserResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserRequest request);
    UserResponse toUserResponse(User user);
    UserDto toUserDto(User user);
}