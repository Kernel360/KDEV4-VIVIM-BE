package com.welcommu.modulecommon.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {

    // common
    FORBIDDEN_ACCESS("A003", "API 요청 권한이 없습니다."),
    SERVER_ERROR("BO001", "서버에서 오류가 발생했습니다."),

    // company
    NOT_FOUND_COMPANY("C001", "회사를 찾을 수 없습니다."),
    YOUR_ARE_NOT_DEVELOPER("C002", "개발사만 사용할 수 있는 API 요청입니다."),
    YOUR_ARE_NOT_CUSTOMER("C003", "고객사만 사용할 수 있는 API 요청입니다."),

    // user
    NOT_FOUND_USER("U001", "사용자를 찾을 수 없습니다."),

    // auth
    INVALID_TOKEN("T001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("T002", "토큰이 만료되었습니다."),
    INVALID_USERID_TYPE("T003", "유효하지 않은 userId 타입입니다."),
    INVALID_CREDENTIALS("T004", "이메일 또는 비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN_TYPE("T005", "유효하지 않은 토큰 타입입니다."),

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

    // approval
    NOT_FOUND_APPROVAL_PROPOSAL("AP001", "승인요청을 찾을 수 없습니다."),
    NOT_FOUND_APPROVAL_DECISION("AP002", "승인응답을 찾을 수 없습니다."),
    NOT_FOUND_APPROVAL_APPROVER("AP003", "지정된 승인권자를 찾을 수 없습니다."),
    YOUR_ARE_NOT_APPROVER("AP005", "해당 승인요청의 승인권자가 아닙니다."),
    NO_APPROVER_ASSIGNED("AP006", "지정된 승인권자가 있어야 승인요청을 보낼 수 있습니다."),
    DUPLICATED_APPROVAL_APPROVER("AP007", "이미 등록된 승인권자입니다."),
    PROPOSAL_NOT_SENT_YET("AP008", "승인 요청이 아직 전송되지 않아 응답할 수 없습니다."),

    // file
    NOT_FOUND_FILE("F001", "파일을 찾을 수 없습니다."),

    // link
    NOT_FOUND_LINK("L001", "링크를 찾을 수 없습니다."),

    // admin inquiry
    NOT_FOUND_INQUIRY("I001", "문의를 찾을 수 없습니다."),
    NOT_FOUND_INQUIRY_COMMENT("I002", "답변을 찾을 수 없습니다.");

    private final String code;
    private final String errorMessage;

    CustomErrorCode(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
