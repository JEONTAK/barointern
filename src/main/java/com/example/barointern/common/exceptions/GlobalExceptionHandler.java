package com.example.barointern.common.exceptions;

import com.example.barointern.common.web.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(CustomException ex) {
        return getErrorResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String firstErrorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElseThrow(() -> new IllegalStateException("검증 에러가 반드시 존재해야 합니다."));
        return getErrorResponse(HttpStatus.BAD_REQUEST, firstErrorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRunTimeException(RuntimeException ex) {
        log.error("RuntimeException: " + ex.getMessage());
        return getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다. 관리자에게 문의하세요.");
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(HttpStatus status, String message) {
        ErrorResponse response = ErrorResponse.from(status.name(), status.value(), message);
        return new ResponseEntity<>(response, status);
    }
}