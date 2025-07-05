package com.example.barointern.common.enums;

import com.example.barointern.common.exceptions.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum UserRole {
    USER, ADMIN

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "유효하지 않은 UserRole 입니다."));
    }
}
