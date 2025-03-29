package com.welcommu.modulecommon.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {

    private final CustomErrorCode errorCode;

    public CustomException(CustomErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    // 예외 객체를 추가로 받는 생성자
    public CustomException(CustomErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrorMessage(), cause);  // 부모 클래스에 메시지와 예외 객체 전달
        this.errorCode = errorCode;
        log.error("Error occurred: {}", errorCode.getErrorMessage(), cause);  // 예외 로그 출력
    }
}
