package com.example.barointern.common.aop;

import com.example.barointern.common.annotation.AuthPermission;
import com.example.barointern.common.enums.UserRole;
import com.example.barointern.common.exceptions.CustomException;
import com.example.barointern.common.web.dto.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthPermissionAspect {

    private final HttpServletRequest request;

    @Before("@annotation(authPermission)")
    public void checkPermission(AuthPermission authPermission) {
        AuthUser authUser = (AuthUser) request.getAttribute("authUser");

        if (authUser == null) {
            throw new CustomException(HttpStatus.FORBIDDEN, "이 작업을 수행할 권한이 없습니다.");
        }

        UserRole requiredRole = authPermission.role();
        if (!authUser.getUserRole().equals(requiredRole)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "이 작업을 수행할 권한이 없습니다.");
        }
    }
}