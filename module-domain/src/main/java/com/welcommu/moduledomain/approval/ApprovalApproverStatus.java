package com.welcommu.moduledomain.approval;

public enum ApprovalApproverStatus {
    NOT_RESPONDED,               // 아직 응답 안함
    APPROVER_APPROVED,          // 반려응답만 있고, 승인응답이 하나도 없는 경우
    APPROVER_REJECTED              // 승인응답이 하나라도 있는 경우
}
