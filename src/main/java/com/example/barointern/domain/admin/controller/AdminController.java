package com.example.barointern.domain.admin.controller;

import com.example.barointern.common.annotation.AuthPermission;
import com.example.barointern.common.enums.UserRole;
import com.example.barointern.domain.admin.dto.request.ChangeUserRoleRequest;
import com.example.barointern.domain.admin.dto.response.ChangeUserRoleResponse;
import com.example.barointern.domain.admin.service.AdminService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 API", description = "USER 역할 변경, 관리자용 유저 검색 API를 테스트할 수 있습니다.")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "유저 역할변경 API", description = "유저의 역할을 변경하는 API입니다. 관리자만 요청이 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 역할 변경 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChangeUserRoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일 형식 오류 또는 유효하지 않은 역할)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족 (ADMIN 권한 필요)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "해당 이메일의 유저를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @PatchMapping("/roles")
    @AuthPermission(role = UserRole.ADMIN)
    public ResponseEntity<ChangeUserRoleResponse> changeUserRole(@Valid @RequestBody ChangeUserRoleRequest request) {
        User user = adminService.changeUserRole(request.getEmail(), request.getUserRole());
        ChangeUserRoleResponse response = ChangeUserRoleResponse.of(user.getEmail(), user.getUserRole());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
