package com.welcommu.moduleservice.projectProgess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProgressApprovalStatusOverallResponse {
    private int totalStageCount;
    private int completedStageCount;
    private float currentStageProgressRate; // 0.0 ~ 1.0
    private float overallProgressRate; // 0.0 ~ 100.0
}
