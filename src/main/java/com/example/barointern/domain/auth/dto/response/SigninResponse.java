package com.example.barointern.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponse {

    private final String token;

    private SigninResponse(String token) {
        this.token = token;
    }

    public static SigninResponse of(String token) {
        return new SigninResponse(token);
    }
}
