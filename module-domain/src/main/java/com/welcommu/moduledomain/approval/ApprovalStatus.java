package com.welcommu.moduledomain.approval;

public enum ApprovalStatus {

    APPROVAL_BEFORE_PROPOSAL, // 최초의 승인요청 전
    APPROVAL_AFTER_PROPOSAL, // 최초의 승인요청 후 (고객사가 읽기 전, 거절응답 발생할 경우 여기로 돌아옴)

    APPROVAL_IN_PROGRESS_DECISION, // 승인응답 진행중 (승인권자는 여러명일 수 있음)
    APPROVAL_REJECTED_DECISION, // 거절응답
    APPROVAL_APPROVED_DECISION // 승인응답
}
