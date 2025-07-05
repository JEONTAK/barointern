package com.example.barointern.domain.auth.dto.response;

import com.example.barointern.common.enums.UserRole;
import lombok.Getter;

@Getter
public class SignupResponse {

    private final String email;
    private final UserRole userRole;

    private SignupResponse(String email, UserRole userRole) {
        this.email = email;
        this.userRole = userRole;
    }

    public static SignupResponse of(String email, UserRole userRole) {
        return new SignupResponse(email, userRole);
    }
}
