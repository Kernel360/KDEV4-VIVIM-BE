package com.welcommu.moduleapi.project;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.user.CustomUserDetails;
import com.welcommu.moduleservice.project.ProjectService;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.dto.ProjectDeleteRequest;
import com.welcommu.moduleservice.project.dto.ProjectSummaryResponse;
import com.welcommu.moduleservice.project.dto.ProjectUpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse> createProject(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody ProjectCreateRequest dto
    ) {

        projectService.createProject(userDetails.getUser(), dto);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트가 생성되었습니다."));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse> updateProject(
        @PathVariable Long projectId,
        @RequestBody ProjectUpdateRequest dto
    ) {
        projectService.updateProject(projectId, dto);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 수정되었습니다."));
    }

    @GetMapping()
    public ResponseEntity<List<ProjectSummaryResponse>> readProjects(@RequestParam Long userId) {
       List<ProjectSummaryResponse> projects = projectService.readProjectsByUser(userId);
       return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse> DeleteProject(
        @PathVariable Long projectId,
        @RequestBody ProjectDeleteRequest dto
    ) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }

    // TODO
    // 모든 프로젝트 조회(관리자)





}
