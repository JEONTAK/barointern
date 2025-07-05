package com.example.barointern.common.consts;

import java.util.Map;

public interface Const {

    Long HOUR = 60 * 60 * 1000L;

    //로그인 없이 방문 가능한 페이지
    Map<String, String[]> WHITE_LIST = Map.of(
            "GET", new String[]{
                    "/swagger-ui/*",
                    "/v*/api-docs/**",
            },
            "POST", new String[]{
                    "/api/v*/auth/signup/*",
                    "/api/v*/auth/signin",
                    "/swagger-ui/*",
                    "/v*/api-docs/**",
            }
    );
}
