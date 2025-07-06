package com.example.barointern.domain.admin.service;

import com.example.barointern.common.enums.UserRole;
import com.example.barointern.common.exceptions.CustomException;
import com.example.barointern.domain.user.entity.User;
import com.example.barointern.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public User changeUserRole(String email, UserRole userRole) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 이메일을 가진 유저가 존재하지 않습니다."));

        if (user.getUserRole().equals(userRole)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "해당 유저는 이미 해당 역할을 가지고 있습니다.");
        }

        user.changeRole(userRole);
        return userRepository.save(user);
    }
}
