package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectStatus;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final ProjectUserRepository projectUserRepository;


    @Transactional
    public void createProject(ProjectCreateRequestDto dto) {
        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("생성자 없음"));

        Project project = dto.toEntity(creator);
        projectRepository.save(project);

        for (ProjectUserRequestDto userDto : dto.getUsers()) {
            User user = userRepository.findById(userDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));

            ProjectUser projectUser = userDto.toEntity(project, user);

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

    public List<ProjectSummaryDto> readProjectsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        List<ProjectUser> projectUsers = projectUserRepository.findByUser(user);

        return projectUsers.stream()
                .filter(pu -> pu.getProject().getStatus() == ProjectStatus.ACTIVE) // ACTIVE인 상태의 프로젝트만 필터링
                .map(pu -> {
                    Project p = pu.getProject();
                    return new ProjectSummaryDto(
                            p.getId(),
                            p.getName(),
                            p.getStatus(),
                            pu.getProjectUserRole()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProject(Long projectId, ProjectDeleteRequestDto dto) {
        Project original = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));

        User deleter = userRepository.findById(dto.getModifierId())
                .orElseThrow(() -> new IllegalArgumentException("삭제자 없음"));

        Project deleted = original.toBuilder()
                .status(ProjectStatus.DELETED)
                .deletedAt(LocalDateTime.now())
                .deleter(deleter)
                .modifiedAt(LocalDateTime.now())         // 수정 시간도 갱신
                .modifier(deleter)
                .build();

        projectRepository.save(deleted);

        // 참여자들도 필요 시 정리
        //projectUserRepository.deleteByProject(project); // 삭제된 프로젝트의 참여자 초기화
    }
}
