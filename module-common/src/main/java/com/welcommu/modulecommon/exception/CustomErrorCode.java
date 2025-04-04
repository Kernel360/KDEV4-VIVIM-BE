package com.welcommu.modulecommon.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {

    // company
    NOT_FOUND_COMPANY("COM001", "회사를 찾을 수 없습니다."),
    INVALID_TOKEN("TOKEN001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("TOKEN002","토큰이 만료되었습니다."),
    SERVER_ERROR("SERVER001","서버에서 오류가 발생했습니다."),
    UNAUTHORIZED("AUTH001","권한이 없습니다."),

    // post
    NOT_FOUND_POST("POST001", "게시글을 찾을 수 없습니다."),

    // comment
    NOT_FOUND_COMMENT("COMMENT001","댓글을 찾을 수 없습니다."),

    // file
    NOT_FOUND_FILE("POST001", "게시글을 찾을 수 없습니다."),

    // link
    NOT_FOUND_LINK("POST001", "게시글을 찾을 수 없습니다."),

    // project
    NOT_FOUND_PROJECT("P001", "프로젝트를 찾을 수 없습니다."),

    // project progress
    NOT_FOUND_PROGRESS("PG001", "프로젝트 단계를 찾을 수 없습니다."),
    MISMATCH_PROJECT_PROGRESS("PG002", "프로젝트와 일치하지 않는 단계입니다."),
    NOT_FOUND_PROGRESS_POSITION("PG003", "프로젝트 단계의 위치를 찾을 수 없습니다.");

    private final String code;
    private final String errorMessage;

    CustomErrorCode(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
