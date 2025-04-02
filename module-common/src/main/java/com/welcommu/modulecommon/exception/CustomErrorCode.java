package com.welcommu.modulecommon.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {
    //project progress
    NOT_FOUND_PROGRESS("PG001", "찾을 수 없는 프로젝트 단계입니다."),
    // 회사 관련 에러 코드
    NOT_FOUND_COMPANY("COMPANY001", "회사를 찾을 수 없습니다."),
    INVALID_TOKEN("TOKEN001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("TOKEN002","토큰이 만료되었습니다."),
    SERVER_ERROR("SERVER001","서버에서 오류가 발생했습니다."),
    UNAUTHORIZED("AUTHORIZATION001","권한이 없습니다."),

    NOT_FOUND_POST("POST001", "게시글을 찾을 수 없습니다."),

    NOT_FOUND_COMMENT("COMMENT001","댓글을 찾을 수 없습니다.");
    private final String code;
    private final String errorMessage;

    CustomErrorCode(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
