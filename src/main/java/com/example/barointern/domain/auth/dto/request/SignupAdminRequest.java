package com.example.barointern.domain.auth.dto.request;

import com.example.barointern.common.consts.Const;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupAdminRequest {

    @Schema(description = "이메일", example = "admin@example.com")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
    private String email;

    @Schema(description = "비밀번호", example = "password1234")
    @Pattern(regexp = Const.PASSWORD_PATTERN, message = "비밀번호 형식에 맞지 않습니다.")
    private String password;

    @Schema(description = "관리자 검증 비밀번호", example = "1q2w3e4r")
    @NotBlank(message = "관리자 검증 비밀번호는 공백이 아니어야 합니다.")
    private String adminVerifyPassword;
}
