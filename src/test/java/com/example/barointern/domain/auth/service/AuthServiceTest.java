package com.example.barointern.domain.auth.service;

import com.example.barointern.common.consts.Const;
import com.example.barointern.common.encoder.PasswordEncoder;
import com.example.barointern.common.enums.UserRole;
import com.example.barointern.common.exceptions.CustomException;
import com.example.barointern.common.jwt.JwtUtil;
import com.example.barointern.domain.user.entity.User;
import com.example.barointern.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.of(1L, "user@example.com", "encodedPassword", UserRole.USER);
    }

    @Test
    void 유저_회원가입_성공() {
        // Given
        String email = "user@example.com";
        String password = "password123";
        when(userRepository.existByEmail(email)).thenReturn(false);
        when(userRepository.generateId()).thenReturn(1L);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = authService.signupUser(email, password);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(UserRole.USER, result.getUserRole());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).existByEmail(email);
        verify(userRepository).generateId();
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void 유저_회원가입시_이미_존재하는_이메일인_경우_실패() {
        // Given
        String email = "user@example.com";
        when(userRepository.existByEmail(email)).thenReturn(true);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                authService.signupUser(email, "password123"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("해당 이메일은 이미 존재합니다.", exception.getMessage());
        verify(userRepository).existByEmail(email);
        verify(userRepository, never()).generateId();
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void 관리자_회원가입_성공() {
        // Given
        String email = "admin@example.com";
        String password = "password123";
        String adminVerifyPassword = Const.ADMIN_VERIFY_PASSWORD;
        when(userRepository.existByEmail(email)).thenReturn(false);
        when(userRepository.generateId()).thenReturn(2L);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(
                User.of(2L, email, "encodedPassword", UserRole.ADMIN));

        // When
        User result = authService.signupAdmin(email, password, adminVerifyPassword);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(UserRole.ADMIN, result.getUserRole());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).existByEmail(email);
        verify(userRepository).generateId();
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void 관리자_회원가입시_이미_존재하는_이메일인_경우_실패() {
        // Given
        String email = "admin@example.com";
        when(userRepository.existByEmail(email)).thenReturn(true);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                authService.signupAdmin(email, "password123", Const.ADMIN_VERIFY_PASSWORD));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("해당 이메일은 이미 존재합니다.", exception.getMessage());
        verify(userRepository).existByEmail(email);
        verify(userRepository, never()).generateId();
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void 관리자_회원가입시_관리자_검증_비밀번호가_일치하지_않을_경우_실패() {
        // Given
        String email = "admin@example.com";
        String wrongAdminPassword = "wrongPassword";
        when(userRepository.existByEmail(email)).thenReturn(false);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                authService.signupAdmin(email, "password123", wrongAdminPassword));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("관리자 검증 비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(userRepository).existByEmail(email);
        verify(userRepository, never()).generateId();
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void 로그인_성공() {
        // Given
        String email = "user@example.com";
        String password = "password123";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        when(jwtUtil.createToken(1L, email, UserRole.USER)).thenReturn("jwt-token");

        // When
        String token = authService.signin(email, password);

        // Then
        assertEquals("jwt-token", token);
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, "encodedPassword");
        verify(jwtUtil).createToken(1L, email, UserRole.USER);
    }

    @Test
    void 로그인_시도시_존재하지_않는_이메일인_경우_실패() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                authService.signin(email, "password123"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("해당 이메일을 가진 유저가 존재하지 않습니다.", exception.getMessage());
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).createToken(anyLong(), anyString(), any());
    }

    @Test
    void 로그인_시도시_비밀번호가_일치하지_않을_경우_실패() {
        // Given
        String email = "user@example.com";
        String wrongPassword = "wrongPassword";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, "encodedPassword")).thenReturn(false);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                authService.signin(email, wrongPassword));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(wrongPassword, "encodedPassword");
        verify(jwtUtil, never()).createToken(anyLong(), anyString(), any());
    }
}