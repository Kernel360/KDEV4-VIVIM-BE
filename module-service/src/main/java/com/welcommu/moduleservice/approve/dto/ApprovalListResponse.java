package com.welcommu.moduleservice.approve.dto;

import com.welcommu.moduledomain.approval.Approval;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApprovalListResponse {

    private List<ApprovalResponse> approvalListResponse;

    public static ApprovalListResponse from(List<Approval> approvalList) {

        List<ApprovalResponse> approvalListResponse = approvalList.stream()
            .map(ApprovalResponse::of)
            .toList();

        return ApprovalListResponse.builder()
            .approvalListResponse(approvalListResponse)
            .build();
    }
}
