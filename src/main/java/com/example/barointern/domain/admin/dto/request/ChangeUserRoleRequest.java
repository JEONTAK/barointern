package com.example.barointern.domain.admin.dto.request;

import com.example.barointern.common.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeUserRoleRequest {

    @Schema(description = "역할 변경할 사용자 이메일입니다.", example = "example@example.com")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Schema(description = "변경되는 역할입니다.", example = "ADMIN")
    @NotNull(message = "역할은 필수 값입니다.")
    private UserRole userRole;
}
