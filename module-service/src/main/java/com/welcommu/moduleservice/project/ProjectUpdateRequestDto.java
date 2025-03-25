package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.ProjectStatus;
import com.welcommu.moduledomain.project.ProjectUserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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

    private List<UserRoleDto> users; // 수정 후의 참여자 목록

    @Getter
    @Setter
    public static class UserRoleDto {
        private Long userId;
        private ProjectUserRole role;
    }
}