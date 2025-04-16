package com.welcommu.moduleservice.project;


import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.logging.ProjectAuditService;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.dto.ProjectUserListCreate;
import com.welcommu.moduleservice.project.dto.ProjectUserRoleRequest;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectUserRepository projectUserRepository;

    @Mock
    private ProjectProgressRepository progressRepository;

    @Mock
    private ProjectAuditService projectAuditService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void 프로젝트_생성_성공() {
        // given
        Long creatorId = 10L;
        User user1 = User.builder()
            .id(10L)
            .build();

        User user2 = User.builder()
            .id(11L)
            .build();

        ProjectCreateRequest dto = ProjectCreateRequest.builder()
            .name("테스트 프로젝트")
            .description("테스트 설명")
            .startDate(LocalDate.of(2025,05,05))
            .endDate(LocalDate.of(2025,05,10))
            .clientUsers(List.of(new ProjectUserListCreate(10L),new ProjectUserListCreate(11L)))
            .clientManagers(List.of())
            .devUsers(List.of())
            .devManagers(List.of())
            .build();

        Project mockSavedProject = Project.builder().id(100L).build();
        when(projectRepository.save(any(Project.class))).thenReturn(mockSavedProject);
        when(userRepository.findById(10L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(11L)).thenReturn(Optional.of(user2));

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);

        // when
        projectService.createProject(dto, creatorId);

        // then
        verify(projectRepository).save(projectCaptor.capture());
        verify(projectAuditService, times(1)).logCreateAudit(eq(mockSavedProject), eq(creatorId));

        Project captured = projectCaptor.getValue();
        assertEquals("테스트 프로젝트", captured.getName());
        assertEquals("테스트 설명", captured.getDescription());
        assertEquals(LocalDate.of(2025, 5, 5), captured.getStartDate());
        assertEquals(LocalDate.of(2025, 5, 10), captured.getEndDate());
    }
}
