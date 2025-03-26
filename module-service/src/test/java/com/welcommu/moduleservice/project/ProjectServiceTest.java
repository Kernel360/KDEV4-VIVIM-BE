package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.project.*;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.company.CompanyRepository;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.project.Dto.ProjectSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock private ProjectRepository projectRepository;
    @Mock private ProjectUserRepository projectUserRepository;
    @Mock private UserRepository userRepository;
    @Mock private CompanyRepository companyRepository;

    private User creator;
    private User dev;
    private Project createdProject1;
    private Project createdProject2;
    private ProjectUser projectUser1;
    private ProjectUser projectUser2;

    @BeforeEach
    void setup() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Mock Company");

        creator = new User();
        creator.setId(10L);
        creator.setName("생성자");
        creator.setCompany(company);

        dev = new User();
        dev.setId(11L);
        dev.setName("개발자");
        dev.setCompany(company);

        createdProject1 = Project.builder()
                .id(100L)
                .name("테스트 프로젝트 1")
                .description("설명1")
                .status(ProjectStatus.ACTIVE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .creator(creator)
                .isDeleted(false)
                .createdAt(LocalDate.now().atStartOfDay())
                .build();

        createdProject2 = Project.builder()
                .id(101L)
                .name("테스트 프로젝트 2")
                .description("설명2")
                .status(ProjectStatus.ACTIVE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .creator(creator)
                .isDeleted(false)
                .createdAt(LocalDate.now().atStartOfDay())
                .build();

        projectUser1 = ProjectUser.builder()
                .id(1L)
                .project(createdProject1)
                .user(dev)
                .projectUserRole(ProjectUserRole.DEVELOPER)
                .build();

        projectUser2 = ProjectUser.builder()
                .id(2L)
                .project(createdProject2)
                .user(dev)
                .projectUserRole(ProjectUserRole.CLIENT_MANAGER)
                .build();
    }

    @Test
    @DisplayName("readProjectsByUser - 유저가 참여한 프로젝트 2개 조회")
    void readProjectsByUserTest() {
        // given
        when(userRepository.findById(dev.getId())).thenReturn(Optional.of(dev));
        when(projectUserRepository.findByUser(dev)).thenReturn(List.of(projectUser1, projectUser2));

        // when
        List<ProjectSummary> result = projectService.readProjectsByUser(dev.getId());

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("projectId")
                .containsExactlyInAnyOrder(createdProject1.getId(), createdProject2.getId());
        assertThat(result).extracting("projectName")
                .containsExactlyInAnyOrder("테스트 프로젝트 1", "테스트 프로젝트 2");
    }
}
