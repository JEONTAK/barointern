package com.example.barointern.domain.auth.service;

import com.example.barointern.common.consts.Const;
import com.example.barointern.common.encoder.PasswordEncoder;
import com.example.barointern.common.enums.UserRole;
import com.example.barointern.common.exceptions.CustomException;
import com.example.barointern.common.jwt.JwtUtil;
import com.example.barointern.domain.user.entity.User;
import com.example.barointern.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User signupUser(String email, String password) {
        //이메일이 이미 존재하는지 확인
        if(userRepository.existByEmail(email)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "해당 이메일은 이미 존재합니다.");
        }

        //ID 생성
        Long id = userRepository.generateId();

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        return userRepository.save(User.of(id, email, encodedPassword, UserRole.USER));
    }

    public User signupAdmin(String email, String password, String adminVerifyPassword) {
        //이메일이 이미 존재하는지 확인
        if(userRepository.existByEmail(email)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "해당 이메일은 이미 존재합니다.");
        }

        if(!adminVerifyPassword.equals(Const.ADMIN_VERIFY_PASSWORD)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "관리자 검증 비밀번호가 일치하지 않습니다.");
        }

        //ID 생성
        Long id = userRepository.generateId();

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        return userRepository.save(User.of(id, email, encodedPassword, UserRole.ADMIN));
    }

    public String signin(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 이메일을 가진 유저가 존재하지 않습니다."));
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }
        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
    }
}
