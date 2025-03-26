package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.project.*;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.company.CompanyRepository;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.user.UserRepository;
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

@ExtendWith(MockitoExtension.class) // Mockito 기반 단위 테스트를 위한 설정
class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService; // 테스트 대상 (Service 계층)

    // 아래는 모두 Mock 처리할 의존 객체들 (실제 DB 접근 X)
    @Mock private ProjectRepository projectRepository;
    @Mock private ProjectUserRepository projectUserRepository;
    @Mock private UserRepository userRepository;
    @Mock private CompanyRepository companyRepository;

    private User creator;       // 생성자 유저
    private User dev;           // 참여자 유저
    private Project createdProject;    // 생성된 프로젝트 객체
    private ProjectUser projectUser;   // 참여자 매핑 객체

    @BeforeEach
    void setup() {
        // 가짜 회사 생성
        Company company = new Company();
        company.setId(1L);
        company.setName("Mock Company");

        // 프로젝트 생성자 유저 설정
        creator = new User();
        creator.setId(10L);
        creator.setName("생성자");
        creator.setCompany(company);

        // 개발자 유저 설정
        dev = new User();
        dev.setId(11L);
        dev.setName("개발자");
        dev.setCompany(company);

        // 생성된 프로젝트 설정
        createdProject = Project.builder()
                .id(100L)
                .name("테스트 프로젝트")
                .description("설명")
                .status(ProjectStatus.ACTIVE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .creator(creator)
                .isDeleted(false)
                .createdAt(LocalDate.now().atStartOfDay())
                .build();

        // 프로젝트-유저 매핑 객체 설정
        projectUser = ProjectUser.builder()
                .id(1L)
                .project(createdProject)
                .user(dev)
                .projectUserRole(ProjectUserRole.DEVELOPER)
                .build();
    }

    @Test
    @DisplayName("유저 생성 후 프로젝트 생성, 조회까지 단위 테스트")
    void createAndReadProjectForUser() {
        // given
        // 1. 참여자 역할 DTO 설정 (✅ 외부 DTO로 교체)
        ProjectUserRequestDto roleDto = new ProjectUserRequestDto();
        roleDto.setUserId(dev.getId());
        roleDto.setRole(ProjectUserRole.DEVELOPER);

        // 2. 프로젝트 생성 요청 DTO 설정
        ProjectCreateRequestDto createDto = new ProjectCreateRequestDto();
        createDto.setName("테스트 프로젝트");
        createDto.setDescription("설명");
        createDto.setProjectStatus(ProjectStatus.ACTIVE);
        createDto.setStartDate(LocalDate.now());
        createDto.setEndDate(LocalDate.now().plusDays(10));
        createDto.setCreatorId(creator.getId());
        createDto.setUsers(List.of(roleDto));  // ✅ List<ProjectUserRequestDto>

        // 3. Mock 객체 리턴값 설정
        when(userRepository.findById(creator.getId())).thenReturn(Optional.of(creator));
        when(userRepository.findById(dev.getId())).thenReturn(Optional.of(dev));
        when(projectRepository.save(any())).thenReturn(createdProject);
        when(projectUserRepository.save(any())).thenReturn(projectUser);
        when(projectUserRepository.findByUser(dev)).thenReturn(List.of(projectUser));

        // when
        projectService.createProject(createDto);
        List<ProjectSummaryDto> result = projectService.readProjectsByUser(dev.getId());

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProjectId()).isEqualTo(createdProject.getId());
        assertThat(result.get(0).getProjectName()).isEqualTo("테스트 프로젝트");
        assertThat(result.get(0).getStatus()).isEqualTo(ProjectStatus.ACTIVE);
        assertThat(result.get(0).getMyRole()).isEqualTo(ProjectUserRole.DEVELOPER);
    }
}
