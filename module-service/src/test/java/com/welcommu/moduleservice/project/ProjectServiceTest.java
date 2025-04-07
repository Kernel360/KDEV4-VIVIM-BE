package com.welcommu.moduleservice.project;


import com.welcommu.moduledomain.project.Project;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.when;

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

    public ProjectServiceTest() {
        MockitoAnnotations.openMocks(this); // 초기화
    }

    @Test
    void 프로젝트_생성_성공() {
        // given
        ProjectCreateRequest request = new ProjectCreateRequest();
        // 필드 세팅 필요: 예) request.setTitle("테스트 프로젝트");

        Project mockProject = Project.builder()
                .title("테스트 프로젝트")
                .isDeleted(false)
                .build();

        when(projectRepository.save(any(Project.class))).thenReturn(mockProject);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(projectUserRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        // when
        projectService.createProject(request);

        // then
        verify(projectRepository, times(1)).save(any(Project.class));
        verify(projectUserRepository, times(1)).saveAll(anyList());
        verify(progressRepository, atLeastOnce()).save(any()); // 초기 단계 저장
    }
}
