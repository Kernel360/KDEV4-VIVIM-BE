package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;

public record CreateProjectPostCommand(Long projectId, String title, String content, ProjectPostStatus projectPostStatus) {
}
