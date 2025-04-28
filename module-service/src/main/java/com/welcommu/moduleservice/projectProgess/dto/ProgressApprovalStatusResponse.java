package com.welcommu.moduleservice.projectProgess.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProgressApprovalStatusResponse {

    private List<ProgressApprovalStatus> progressList;

    @Getter
    @AllArgsConstructor
    public static class ProgressApprovalStatus {
        private Long progressId;
        private String progressName;
        private Long totalApprovalCount;
        private Long approvedApprovalCount;
        private Float progressRate;
    }
}