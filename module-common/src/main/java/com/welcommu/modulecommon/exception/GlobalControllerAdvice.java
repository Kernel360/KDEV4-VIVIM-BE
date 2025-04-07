package com.welcommu.modulecommon.exception;

import com.welcommu.modulecommon.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handlerValidationException(
        MethodArgumentNotValidException ex) {

        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : ex.getFieldErrors()) {
            log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            errorMessage.append(fieldError.getField()).append(" 필드 : ")
                .append(fieldError.getDefaultMessage()).append(" ");
        }

        return ErrorResponse.error(HttpStatus.BAD_REQUEST, "V001", errorMessage.toString());
    }
}
