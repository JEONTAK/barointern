package com.example.barointern.domain.admin.service;

import com.example.barointern.common.enums.UserRole;
import com.example.barointern.common.exceptions.CustomException;
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
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.of(1L, "user@example.com", "encodedPassword", UserRole.USER);
    }

    @Test
    void 역할_변경_성공() {
        // Given
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = adminService.changeUserRole("user@example.com", UserRole.ADMIN);

        // Then
        assertNotNull(result);
        assertEquals(UserRole.ADMIN, result.getUserRole());
        assertEquals("user@example.com", result.getEmail());
        verify(userRepository).findByEmail("user@example.com");
        verify(userRepository).save(user);
    }

    @Test
    void 존재하지_않는_유저_역할_변경_시도할_경우_실패() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                adminService.changeUserRole("nonexistent@example.com", UserRole.ADMIN));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("해당 이메일을 가진 유저가 존재하지 않습니다.", exception.getMessage());
        verify(userRepository).findByEmail("nonexistent@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void 이미_해당_역할을_가지고_있을_경우_실패() {
        // Given
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                adminService.changeUserRole("user@example.com", UserRole.USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("해당 유저는 이미 해당 역할을 가지고 있습니다.", exception.getMessage());
        verify(userRepository).findByEmail("user@example.com");
        verify(userRepository, never()).save(any());
    }
}