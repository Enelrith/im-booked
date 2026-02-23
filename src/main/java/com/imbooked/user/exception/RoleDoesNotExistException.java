package com.imbooked.user.exception;

import com.imbooked.shared.enums.RoleName;

public class RoleDoesNotExistException extends RuntimeException {
    public RoleDoesNotExistException(RoleName roleName) {
        super("Role with name " + roleName.name() + " does not exist");
    }

    public RoleDoesNotExistException(String message) {
        super(message);
    }
}
