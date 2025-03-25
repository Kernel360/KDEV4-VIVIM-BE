package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.ProjectStatus;
import com.welcommu.moduledomain.project.ProjectUserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectCreateRequestDto {

    private String name;
    private String description;
    private ProjectStatus projectStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long creatorId; // 생성자 ID

    private List<UserRoleDto> users; // 참여자 정보

    @Getter
    @Setter
    public static class UserRoleDto {
        private Long userId;
        private ProjectUserRole role;
    }
}