package com.example.barointern.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninRequest {

    @Schema(description = "이메일", example = "admin@example.com")
    @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
    private String email;

    @Schema(description = "비밀번호", example = "password1234")
    @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
    private String password;
}
