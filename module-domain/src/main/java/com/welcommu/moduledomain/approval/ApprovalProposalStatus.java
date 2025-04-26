package com.welcommu.moduledomain.approval;

public enum ApprovalProposalStatus {

    BEFORE_REQUEST_PROPOSAL,      // 최초 승인 요청 전
    WAITING_FOR_DECISIONS,         // 요청은 보냈으나 대기 중
    REJECTED_BY_ANY_DECISION,      // 하나라도 거절
    APPROVED_BY_ALL_DECISIONS      // 모두 승인
}
