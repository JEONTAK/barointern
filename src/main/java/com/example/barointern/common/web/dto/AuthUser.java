package com.example.barointern.common.web.dto;

import com.example.barointern.common.enums.UserRole;
import lombok.Getter;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final UserRole userRole;

    private AuthUser(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public static AuthUser from(Long id, String email, UserRole userRole) {
        return new AuthUser(id, email, userRole);
    }
}
