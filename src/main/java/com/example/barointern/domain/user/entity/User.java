package com.example.barointern.domain.user.entity;

import com.example.barointern.common.enums.UserRole;
import lombok.Getter;

@Getter
public class User {

    private final Long id;
    private final String email;
    private String password;
    private UserRole userRole;

    private User(Long id, String email, String password, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public static User of(Long id, String email, String password, UserRole userRole) {
        return new User(id, email, password, userRole);
    }

    public void changeRole(UserRole newRole) {
        this.userRole = newRole;
    }
}
