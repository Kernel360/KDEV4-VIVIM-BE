package com.welcommu.modulecommon.exception.dto;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;

    public ErrorResponse(CustomErrorCode errorCode) {
        this.status = HttpStatus.BAD_REQUEST.value();
        this.code = errorCode.getCode();
        this.message = errorCode.getErrorMessage();
    }

    public static ResponseEntity<ErrorResponse> error(CustomException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getErrorMessage())
                .build());
    }

    public static ResponseEntity<ErrorResponse> error(HttpStatus status, String code,
        String message) {
        return ResponseEntity
            .status(status)
            .body(ErrorResponse.builder()
                .status(status.value())
                .code(code)
                .message(message)
                .build());
    }
}
