package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final ProjectUserRepository projectUserRepository;

    @Transactional
    public void createProject(ProjectCreateRequestDto dto) {
        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("생성자 없음"));

        Project project = Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(dto.getProjectStatus())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .creator(creator)
                .build();

        projectRepository.save(project);

        for (ProjectCreateRequestDto.UserRoleDto userRoleDto : dto.getUsers()) {
            User user = userRepository.findById(userRoleDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));

            ProjectUser projectUser = ProjectUser.builder()
                    .project(project)
                    .user(user)
                    .projectUserRole(userRoleDto.getRole())
                    .build();

            projectUserRepository.save(projectUser);
        }
    }

    @Transactional
    public void updateProject(Long projectId, ProjectUpdateRequestDto dto) {
        Project original = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));

        User modifier = userRepository.findById(dto.getModifierId())
                .orElseThrow(() -> new IllegalArgumentException("수정자 없음"));

        Project updated = original.toBuilder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(dto.getProjectStatus())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .modifiedAt(LocalDateTime.now())
                .modifier(modifier)
                .build();

        projectRepository.save(updated);
        projectUserRepository.deleteByProject(original);

        for (ProjectUpdateRequestDto.UserRoleDto userRoleDto : dto.getUsers()) {
            User user = userRepository.findById(userRoleDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));

            ProjectUser projectUser = ProjectUser.builder()
                    .project(updated)
                    .user(user)
                    .projectUserRole(userRoleDto.getRole())
                    .build();

            projectUserRepository.save(projectUser);
        }
    }

}
