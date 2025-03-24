package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectManager;
import com.welcommu.moduledomain.project.UserProject;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.moduledomain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public Project createProject(String name, String description,
                                 List<Long> developerIds, List<Long> clientIds) {

        Set<Long> allParticipantIds = new HashSet<>();
        allParticipantIds.addAll(developerIds);
        allParticipantIds.addAll(clientIds);

        Project project = Project.builder()
                .name(name)
                .description(description)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

//        // 1. 모든 참가자 user_project 저장
//        allParticipantIds.forEach(userId ->
//                project.addUserProject(UserProject.builder()
//                        .user(User.of(userId)) // proxy 객체 사용
//                        .project(project)
//                        .build())
//        );

        // 2. 담당자는 project_manager로도 등록
        developerIds.forEach(userId ->
                project.addManager(ProjectManager.builder()
                        .role(ProjectManager.Role.DEVELOPER)
                        .projectManagerId(userId)
                        .project(project)
                        .build())
        );

        clientIds.forEach(userId ->
                project.addManager(ProjectManager.builder()
                        .role(ProjectManager.Role.CLIENT)
                        .projectManagerId(userId)
                        .project(project)
                        .build())
        );

        return projectRepository.save(project);
    }
}
