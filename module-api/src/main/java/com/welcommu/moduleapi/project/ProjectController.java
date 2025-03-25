package com.welcommu.moduleapi.project;

import com.welcommu.moduleservice.project.ProjectCreateRequestDto;
import com.welcommu.moduleservice.project.ProjectService;
import com.welcommu.moduleservice.project.ProjectUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
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


}
