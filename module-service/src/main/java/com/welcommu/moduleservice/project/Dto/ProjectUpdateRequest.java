package com.welcommu.moduleservice.project.Dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectStatus;
import com.welcommu.moduledomain.project.ProjectUserRole;
import com.welcommu.moduledomain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProjectUpdateRequest {

    private String name;
    private String description;
    private ProjectStatus projectStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long modifierId; // 수정자 ID

    private List<ProjectUserRequest> users;

    @Getter
    @Setter
    public static class UserRoleDto {
        private Long userId;
        private ProjectUserRole role;
    }

    public Project toEntity(Project project, User modifier) {
        return project.toBuilder()
                .id(project.getId())
                .name(this.name)
                .description(this.description)
                .status(this.projectStatus)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .modifiedAt(LocalDateTime.now())
                .modifier(modifier)
                .build();
    }
}