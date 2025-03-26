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
public class ProjectDeleteRequestDto {

    private String name;
    private String description;
    private ProjectStatus projectStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long modifierId; // 수정자 ID

    private List<ProjectUserRequestDto> users; // 수정 후의 참여자 목록

    @Getter
    @Setter
    public static class UserRoleDto {
        private Long userId;
        private ProjectUserRole role;
    }

    public Project toEntity(User deleter){
        return Project.builder()
                .name(this.getName())
                .description(this.getDescription())
                .status(this.getProjectStatus())
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .deletedAt(LocalDateTime.now())
                .isDeleted(true)
                .deleter(deleter)
                .build();
    }
}