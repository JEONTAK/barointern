package com.example.barointern.common.web.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final String status;
    private final int code;
    private final String message;
    private final LocalDateTime timestamp;

    private ErrorResponse(String status, int code, String message, LocalDateTime timestamp) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ErrorResponse from(String status, int code, String message) {
        return new ErrorResponse(status, code, message, LocalDateTime.now());
    }
}
