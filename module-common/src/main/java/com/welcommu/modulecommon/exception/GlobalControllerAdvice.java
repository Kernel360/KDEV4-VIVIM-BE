package com.welcommu.modulecommon.exception;

import com.welcommu.modulecommon.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ErrorResponse> handlerCustomException(CustomException e) {
        log.error("[CustomException] {} : {}",
            e.getErrorCode().getCode(), e.getErrorCode().getErrorMessage());
        return ErrorResponse.error(e);
    }
}
