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
public class ProjectCreateRequest {

    private String name;
    private String description;
    private ProjectStatus projectStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long creatorId; // 생성자 ID

    private List<ProjectUserRequest> users;

    @Getter
    @Setter
    public static class UserRoleDto {
        private Long userId;
        private ProjectUserRole role;
    }

    public Project toEntity(User creator){
        return Project.builder()
                .name(this.getName())
                .description(this.getDescription())
                .status(this.getProjectStatus())
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .creator(creator)
                .build();
    }

}