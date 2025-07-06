package com.example.barointern.common.consts;

import java.util.Map;

public interface Const {

    Long HOUR = 60 * 60 * 1000L;

    String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,}$";
    String ADMIN_VERIFY_PASSWORD = "1q2w3e4r";

    //로그인 없이 방문 가능한 페이지
    Map<String, String[]> WHITE_LIST = Map.of(
            "GET", new String[]{
                    "/swagger-ui/*",
                    "/v*/api-docs/**",
                    "/docs"
            },
            "POST", new String[]{
                    "/api/v*/auth/signup/*",
                    "/api/v*/auth/signin",
                    "/swagger-ui/*",
                    "/v*/api-docs/**",
                    "/docs"
            }
    );
}
