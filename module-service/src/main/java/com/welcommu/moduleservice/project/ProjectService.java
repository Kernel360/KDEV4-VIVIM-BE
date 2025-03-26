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
                .orElseThrow(() -> new IllegalArgumentException("프로젝트 생성자 ID 없음"));

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

        // DTO를 통해 기존 객체 값 주입
        dto.applyTo(original, modifier);

        //트랜잭션 커밋 시점에 자동으로 변경 사항을 감지하고 DB에 반영하기에 repository.save 필요 없음

        // 참여자 추가 후 재등록
        projectUserRepository.deleteByProject(original);

        for (ProjectUserRequestDto userDto : dto.getUsers()) {
            User user = userRepository.findById(userDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));

            projectUserRepository.save(userDto.toEntity(original, user));
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
                    return ProjectSummaryDto.of(p, pu);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProject(Long projectId, ProjectDeleteRequestDto dto) {
        Project original = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));

        User deleter = userRepository.findById(dto.getModifierId())
                .orElseThrow(() -> new IllegalArgumentException("삭제자 없음"));

        original.setIsDeleted(true);
        original.setDeletedAt(LocalDateTime.now());
        original.setDeleter(deleter);

        //JPA는 @Transactional 범위 안에서 영속 상태(persistent state)인 엔티티의 필드가 변경되면
        //트랜잭션 커밋 시점에 자동으로 변경 사항을 감지하고 DB에 반영하기에 repository.save 필요 없음
    }
}
