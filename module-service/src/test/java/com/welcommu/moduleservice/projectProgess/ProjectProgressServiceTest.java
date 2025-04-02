package com.welcommu.moduleservice.projectProgess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleservice.projectProgess.dto.ProgressCreateRequest;
import com.welcommu.moduleservice.projectProgess.dto.ProgressListResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressUpdateRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProjectProgressServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectProgressRepository progressRepository;

    @InjectMocks
    private ProjectProgressService projectProgressService;

    private Project project;
    private ProjectProgress projectProgress;

    @BeforeEach
    public void setUp() {

        project = new Project();
        project.setId(1L);
        project.setName("임의의 프로젝트");
        project.setCreatedAt(LocalDateTime.now());

        projectProgress = new ProjectProgress();
        projectProgress.setId(1L);
        projectProgress.setName("임의의 프로젝트 단계");
        projectProgress.setProject(project);
        projectProgress.setPosition(1.0f);

        log.info("\n테스트 : Setup complete, project id {} and initial progress created", project.getId());
    }

    @Test
    public void testCreateProgress() {

        Long projectId = 2L;
        log.info("\n테스트 : CreateProgress with projectId: {}", projectId);

        ProgressCreateRequest request = mock(ProgressCreateRequest.class);
        ProjectProgress progressRequest = new ProjectProgress();
        progressRequest.setName("생성된 단계");

        when(request.toEntity(project)).thenReturn(progressRequest);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(progressRepository.findMaxPositionByProjectId(projectId)).thenReturn(Optional.of(6.0f));

        projectProgressService.createProgress(projectId, request);
        log.info("\n테스트 : After createProgress: progress position is {}", progressRequest.getPosition());

        assertEquals("생성된 단계", progressRequest.getName());
        assertEquals(7.0f, progressRequest.getPosition()); //Default ProjectProgress 에 1개 추가되어 있는 프로젝트
        verify(progressRepository).save(progressRequest);

        log.info("\n테스트 : Completed testCreateProgress successfully.");
    }

    @Test
    public void testUpdateProgress_Success() {
        Long projectId = 1L;
        Long progressId = 1L;

        ProgressUpdateRequest request = new ProgressUpdateRequest();

        request.setName("매칭되는 상황에서 수정된 단계");
        request.setPosition(4.5f);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(progressRepository.findById(progressId)).thenReturn(Optional.of(projectProgress));

        log.info("Before update: projectProgress name = {}", projectProgress.getName());
        log.info("Before update: projectProgress position = {}", projectProgress.getPosition());

        projectProgressService.updateProgress(projectId, progressId, request);

        log.info("After update: projectProgress name = {}", projectProgress.getName());
        log.info("After update: projectProgress position = {}", projectProgress.getPosition());

        assertEquals("매칭되는 상황에서 수정된 단계", projectProgress.getName());
        verify(progressRepository).save(projectProgress);
    }

    @Test
    public void testUpdateProgress_Mismatch() {
        Long projectId = 1L;
        Long progressId = 1L;
        ProgressUpdateRequest request = new ProgressUpdateRequest();
        projectProgress.setName(request.getName());

        Project differentProject = new Project();
        differentProject.setId(2L);
        differentProject.setName("매칭되지 않는 경우에 수정된 단계");
        differentProject.setCreatedAt(project.getCreatedAt());
        projectProgress.setProject(differentProject);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(progressRepository.findById(progressId)).thenReturn(Optional.of(projectProgress));

        CustomException exception = assertThrows(CustomException.class, () -> {
            projectProgressService.updateProgress(projectId, progressId, request);
        });
        assertEquals(CustomErrorCode.MISMATCH_PROJECT_PROGRESS, exception.getErrorCode());
    }

    @Test
    public void testDeleteProgress_Success() {
        Long projectId = 1L;
        Long progressId = 1L;
        projectProgress.setProject(project);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(progressRepository.findById(progressId)).thenReturn(Optional.of(projectProgress));

        projectProgressService.deleteProgress(projectId, progressId);

        verify(progressRepository).delete(projectProgress);
    }

    @Test
    public void testDeleteProgress_Mismatch() {
        Long projectId = 1L;
        Long progressId = 1L;

        // 불일치 상황을 위해 다른 프로젝트 정보 설정
        Project differentProject = new Project();
        differentProject.setId(2L);
        differentProject.setName("매칭되지 않는 경우에 삭제된 단계");
        differentProject.setCreatedAt(project.getCreatedAt());
        projectProgress.setProject(differentProject);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(progressRepository.findById(progressId)).thenReturn(Optional.of(projectProgress));

        CustomException exception = assertThrows(CustomException.class, () -> {
            projectProgressService.deleteProgress(projectId, progressId);
        });
        assertEquals(CustomErrorCode.MISMATCH_PROJECT_PROGRESS, exception.getErrorCode());
    }

    @Test
    public void testGetProgressList() {
        Long projectId = 1L;
        List<ProjectProgress> progressList = Collections.singletonList(projectProgress);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(progressRepository.findByProject(project)).thenReturn(progressList);

        ProgressListResponse response = projectProgressService.getProgressList(projectId);
        assertNotNull(response);
    }
}