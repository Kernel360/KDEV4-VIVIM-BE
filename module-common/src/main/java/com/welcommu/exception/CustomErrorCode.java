package com.welcommu.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {
    //project
    NOT_FOUND_PROJECT("P001", "찾을 수 없는 프로젝트입니다."),

    //project progress
    NOT_FOUND_PROGRESS("PG001", "찾을 수 없는 프로젝트 단계입니다."),
    MISMATCH_PROJECT_PROGRESS("P002", "프로젝트와 일치하지 않는 단계입니다.");

    private final String code;
    private final String errorMessage;

    CustomErrorCode(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
