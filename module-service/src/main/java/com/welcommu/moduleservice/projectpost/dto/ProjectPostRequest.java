package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostRequest {
    private Long id;
    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;
}
