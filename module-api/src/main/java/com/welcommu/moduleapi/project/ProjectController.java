package com.welcommu.moduleapi.project;

import com.welcommu.dto.ApiResponse;
import com.welcommu.moduleservice.project.*;
import com.welcommu.moduleservice.project.Dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.Dto.ProjectDeleteRequest;
import com.welcommu.moduleservice.project.Dto.ProjectSummary;
import com.welcommu.moduleservice.project.Dto.ProjectUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity<ApiResponse> createProject(@RequestBody ProjectCreateRequest dto) {
        projectService.createProject(dto);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트가 생성되었습니다."));
    }

    // 프로젝트 수정
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse> updateProject(@PathVariable Long projectId,
                                              @RequestBody ProjectUpdateRequest dto) {
        projectService.updateProject(projectId, dto);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 수정되었습니다."));
    }

    // 특정 유저 별 프로젝트 조회
    @GetMapping()
    public ResponseEntity<List<ProjectSummary>> readProjects(@RequestBody Long userId) {
       List<ProjectSummary> projects = projectService.readProjectsByUser(userId);
       return ResponseEntity.ok(projects);
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse> DeleteProject(@PathVariable Long projectId,@RequestBody ProjectDeleteRequest dto) {
        projectService.deleteProject(projectId, dto);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 삭제었습니다."));
    }

    // TODO
    // 모든 프로젝트 조회(관리자)





}
