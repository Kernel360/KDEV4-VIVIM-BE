package com.welcommu.modulecommon.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {

    // common
    FORBIDDEN_ACCESS("A003", "API 요청 권한이 없습니다."),
    SERVER_ERROR("BO001", "서버에서 오류가 발생했습니다."),

    // user
    NOT_FOUND_USER("U001", "사용자를 찾을 수 없습니다."),

    // company
    NOT_FOUND_COMPANY("C001", "회사를 찾을 수 없습니다."),
    INVALID_TOKEN("T001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("T002", "토큰이 만료되었습니다."),

    // project
    NOT_FOUND_PROJECT("P001", "프로젝트를 찾을 수 없습니다."),

    // project user
    NOT_FOUND_PROJECT_USER("PU001", "프로젝트가 없거나 해당 프로젝트에 참여하는 사용자를 찾을 수 없습니다."),

    // project post
    NOT_FOUND_POST("PO001", "게시글을 찾을 수 없습니다."),

    // project post comment
    NOT_FOUND_COMMENT("PC001", "댓글을 찾을 수 없습니다."),

    // project progress
    NOT_FOUND_PROGRESS("PG001", "프로젝트 단계를 찾을 수 없습니다."),
    MISMATCH_PROJECT_PROGRESS("PG002", "프로젝트와 일치하지 않는 단계입니다."),
    DUPLICATE_PROGRESS_NAME("PG003", "중복되는 프로젝트명입니다."),
    DUPLICATE_PROGRESS_POSITION("PG004", "중복되는 포지션입니다."),

    // checklist
    NOT_FOUND_CHECKLIST("CH001", "체크리스트를 찾을 수 없습니다."),
    ALREADY_DELETED_CHECKLIST("CH002", "이미 삭제된 체크리스트입니다."),

    // file
    NOT_FOUND_FILE("F001", "파일을 찾을 수 없습니다."),

    // link
    NOT_FOUND_LINK("L001", "링크를 찾을 수 없습니다."),

    // admin inquiry
    NOT_FOUND_INQUIRY("I001", "문의를 찾을 수 없습니다.");
    private final String code;
    private final String errorMessage;

    CustomErrorCode(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
