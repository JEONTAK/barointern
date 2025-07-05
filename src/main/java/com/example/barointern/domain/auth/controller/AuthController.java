package com.example.barointern.domain.auth.controller;

import com.example.barointern.domain.auth.dto.request.SigninRequest;
import com.example.barointern.domain.auth.dto.request.SignupAdminRequest;
import com.example.barointern.domain.auth.dto.request.SignupUserRequest;
import com.example.barointern.domain.auth.dto.response.SigninResponse;
import com.example.barointern.domain.auth.dto.response.SignupResponse;
import com.example.barointern.domain.auth.service.AuthService;
import com.example.barointern.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "인증/인가 API", description = "USER 회원가입, ADMIN 회원가입, 로그인 API를 테스트할 수 있습니다.")
public class AuthController {

    private final AuthService authService;
    @Operation(summary = "일반 사용자 회원가입 API", description = "일반 사용자로 회원가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignupResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일/비밀번호 형식 오류)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 이메일",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/signup/user")
    public ResponseEntity<SignupResponse> signupUser(@Valid @RequestBody SignupUserRequest request) {
        User user = authService.signupUser(request.getEmail(), request.getPassword());
        SignupResponse response = SignupResponse.of(user.getEmail(), user.getUserRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "관리자 회원가입 API", description = "관리자 권한으로 회원가입합니다. 관리자 검증 비밀번호가 필요합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignupResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일/비밀번호/관리자 검증 비밀번호 오류)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 이메일",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/signup/admin")
    public ResponseEntity<SignupResponse> signupAdmin(@Valid @RequestBody SignupAdminRequest request) {
        User user = authService.signupAdmin(request.getEmail(), request.getPassword(), request.getAdminVerifyPassword());
        SignupResponse response = SignupResponse.of(user.getEmail(), user.getUserRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SigninResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일/비밀번호 형식 오류) 또는 비밀번호 일치하지 않음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이메일",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(@Valid @RequestBody SigninRequest request) {
        String token = authService.signin(request.getEmail(), request.getPassword());
        SigninResponse response = SigninResponse.of(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
