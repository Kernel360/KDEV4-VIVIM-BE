package com.welcommu.moduleservice.project;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectUserRepository projectUserRepository;

    @Mock
    private ProjectProgressRepository progressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void 프로젝트_생성_성공() {
        // given
        ProjectCreateRequest dto = mock(ProjectCreateRequest.class);
        Project mockProject = mock(Project.class);
        Project savedProject = mock(Project.class);
        List<ProjectUser> mockUsers = Collections.singletonList(mock(ProjectUser.class));

        when(dto.toEntity()).thenReturn(mockProject);
        when(projectRepository.save(mockProject)).thenReturn(savedProject);
        when(dto.toProjectUsers(eq(savedProject), any())).thenReturn(mockUsers);

        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(User.builder().id(1L).build()));

        // when
        projectService.createProject(dto);

        // then
        verify(projectRepository).save(mockProject);
        verify(progressRepository, times(6)).save(any(ProjectProgress.class)); // 초기 6단계 저장
        verify(projectUserRepository).saveAll(mockUsers);
    }
}
