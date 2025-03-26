package com.welcommu.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {
    //project progress
    NOT_FOUND_PROGRESS("PG001", "찾을 수 없는 프로젝트 단계입니다.");

    private final String code;
    private final String errorMessage;

    CustomErrorCode(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
