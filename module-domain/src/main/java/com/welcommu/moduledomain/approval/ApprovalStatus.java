package com.welcommu.moduledomain.approval;

public enum ApprovalStatus {

    // 개발사가 아직 승인요청을 보내지 않은 상태
    IN_PROGRESS,

    // 개발사가 승인요청을 보낸 상태 (아직 고객사가 승인요청을 읽지 않음)
    REQUEST_APPROVAL,

    // 고객사가 승인요청을 읽은 상태 (아직 개발사가 승인응답은 하지 않음)
    READ,

    // 고객사의 승인요청에 대한 응답 (수정요청 또는 동의)
    REQUEST_MODIFY,
    APPROVED
}
