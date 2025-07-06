package com.example.barointern.domain.admin.dto.response;

import com.example.barointern.common.enums.UserRole;
import lombok.Getter;

@Getter
public class ChangeUserRoleResponse {

    private final String email;
    private final UserRole userRole;

    private ChangeUserRoleResponse(String email, UserRole userRole) {
        this.email = email;
        this.userRole = userRole;
    }

    public static ChangeUserRoleResponse of(String email, UserRole userRole) {
        return new ChangeUserRoleResponse(email, userRole);
    }
}
