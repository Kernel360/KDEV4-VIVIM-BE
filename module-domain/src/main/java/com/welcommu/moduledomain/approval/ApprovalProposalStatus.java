package com.welcommu.moduledomain.approval;

public enum ApprovalProposalStatus {

    DRAFT,              // 승인 요청 준비 중
    UNDER_REVIEW,       // 승인 요청 발송 완료 (응답 수집 중)
    FINAL_APPROVED,     // 전체 승인 완료
    FINAL_REJECTED      // 하나라도 거절 발생
}
