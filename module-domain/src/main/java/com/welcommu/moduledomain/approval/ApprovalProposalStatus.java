package com.welcommu.moduledomain.approval;

public enum ApprovalProposalStatus {

    BEFORE_REQUEST_PROPOSAL,        // 최초 승인 요청 전
    WAITING_FOR_DECISIONS,        // 요청 보낸 후, 아직 아무 응답 없음
    IN_PROGRESS_DECISIONS,        // 일부 응답 도착, 전체 응답 대기 중
    REJECTED_BY_ANY_DECISION,     // 하나라도 거절됨 → 전체 거절로 종료
    APPROVED_BY_ALL_DECISIONS     // 전원 승인 → 전체 승인 완료
}
