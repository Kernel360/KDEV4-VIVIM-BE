package com.welcommu.moduledomain.approval;

public enum ApprovalApproverStatus {
    BEFORE_REQUEST,               // 아직 승인 요청이 보내지지 않은 승인권자
    WAITING_FOR_RESPONSE,          // 요청은 보냈지만 응답은 없는 상태
    REQUEST_MODIFICATION,          // 수정 요청(반려)한 승인권자
    COMPLETE_APPROVED              // 승인 완료한 승인권자
}
