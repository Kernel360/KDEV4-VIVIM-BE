package com.welcommu.modulecommon.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {
    //project progress
    NOT_FOUND_PROGRESS("PG001", "찾을 수 없는 프로젝트 단계입니다."),
    // 회사 관련 에러 코드
    NOT_FOUND_COMPANY("COMPANY001", "회사를 찾을 수 없습니다.");

    private final String code;
    private final String errorMessage;

    CustomErrorCode(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
