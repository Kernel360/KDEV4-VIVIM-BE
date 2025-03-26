package com.welcommu.moduleapi.project;


import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduleservice.project.*;
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
    public ResponseEntity<Void> createProject(@RequestBody ProjectCreateRequestDto dto) {
        projectService.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 프로젝트 수정
    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(@PathVariable Long projectId,
                                              @RequestBody ProjectUpdateRequestDto dto) {
        projectService.updateProject(projectId, dto);
        return ResponseEntity.ok().build();
    }

    // 특정 유저 별 프로젝트 조회
    @GetMapping()
    public ResponseEntity<List<ProjectSummaryDto>> readProjects(@RequestBody Long userId) {
       List<ProjectSummaryDto> projects = projectService.readProjectsByUser(userId);
       return ResponseEntity.ok(projects);
    }

    // TODO
    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> DeleteProject(@PathVariable Long projectId,@RequestBody ProjectDeleteRequestDto dto) {
        projectService.deleteProject(projectId, dto);
        return ResponseEntity.ok().build();
    }

    // TODO
    // 모든 프로젝트 조회(관리자)





}
