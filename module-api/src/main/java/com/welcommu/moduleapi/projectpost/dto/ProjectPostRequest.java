package com.welcommu.moduleapi.projectpost.dto;


import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostRequest {
    private Long id;
    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;
}
