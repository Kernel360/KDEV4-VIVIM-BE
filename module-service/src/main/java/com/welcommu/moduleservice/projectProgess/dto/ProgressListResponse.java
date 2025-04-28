package com.welcommu.moduleservice.projectProgess.dto;

import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProgressListResponse {

    private List<ProgressResponse> progressList;

    public static ProgressListResponse of(List<ProjectProgress> progressList) {

        List<ProgressResponse> progressResponse = progressList.stream()
            .map(ProgressResponse::from)
            .toList();

        return ProgressListResponse.builder()
            .progressList(progressResponse)
            .build();
    }
}
