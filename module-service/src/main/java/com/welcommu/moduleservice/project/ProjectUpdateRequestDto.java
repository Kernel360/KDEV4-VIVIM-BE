package com.welcommu.moduleservice.project;

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
public class ProjectUpdateRequestDto {

    private String name;
    private String description;
    private ProjectStatus projectStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long modifierId; // 수정자 ID

    private List<ProjectUserRequestDto> users;

    @Getter
    @Setter
    public static class UserRoleDto {
        private Long userId;
        private ProjectUserRole role;
    }

    public void applyTo(Project project, User modifier) {
        project.setName(this.name);
        project.setDescription(this.description);
        project.setStatus(this.projectStatus);
        project.setStartDate(this.startDate);
        project.setEndDate(this.endDate);
        project.setModifiedAt(LocalDateTime.now());
        project.setModifier(modifier);
    }
}