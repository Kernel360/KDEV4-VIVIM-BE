package com.welcommu.moduleservice.projectpost;

import com.welcommu.moduledomain.projectpost.ProjectPostStatus;

public record CreateProjectPostCommand(Long projectId, String title, String content, ProjectPostStatus projectPostStatus) {
}
