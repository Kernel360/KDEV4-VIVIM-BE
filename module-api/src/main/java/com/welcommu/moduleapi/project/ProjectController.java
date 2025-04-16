package com.welcommu.moduleapi.project;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduleservice.project.ProjectService;
import com.welcommu.moduleservice.project.dto.ProjectAdminSummaryResponse;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.dto.ProjectDeleteRequest;
import com.welcommu.moduleservice.project.dto.ProjectModifyRequest;
import com.welcommu.moduleservice.project.dto.ProjectUserResponse;
import com.welcommu.moduleservice.project.dto.ProjectUserSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;

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
@Tag(name = "프로젝트 API", description = "프로젝트를 생성, 수정, 삭제시킬 수 있습니다.")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "프로젝트 생성")
    public ResponseEntity<ApiResponse> createProject(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @RequestBody ProjectCreateRequest dto
    ) {
        Long userId = userDetails.getUser().getId();
        projectService.createProject(dto,userId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트가 생성되었습니다."));
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "프로젝트 개별 조회")
    public ResponseEntity<Optional<Project>> readProject(@PathVariable Long projectId
    ) {
        Optional<Project> project = projectService.getProject(projectId);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "프로젝트 수정")
    public ResponseEntity<ApiResponse> modifyProject(
        @PathVariable Long projectId,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @RequestBody ProjectModifyRequest dto
    ) {
        Long userId = userDetails.getUser().getId();
        projectService.modifyProject(projectId, dto, userId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 수정되었습니다."));
    }

    @GetMapping()
    @Operation(summary = "특정 유저 소속 프로젝트 조회")
    public ResponseEntity<List<ProjectUserSummaryResponse>> readProjects(@RequestParam Long userId) {
       List<ProjectUserSummaryResponse> projects = projectService.getProjectsByUser(userId);
       return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "프로젝트 삭제")
    public ResponseEntity<ApiResponse> deleteProject(
        @PathVariable Long projectId,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @RequestBody ProjectDeleteRequest dto
    ) {
        Long userId = userDetails.getUser().getId();
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }

    @GetMapping("/all")
    @Operation(summary = "프로젝트 전체 조회")
    public ResponseEntity<List<ProjectAdminSummaryResponse>> readAllProjectsForAdmin(){
        List<ProjectAdminSummaryResponse> projects = projectService.getProjectList();
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "프로젝트 소속 유저 조회")
    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<ProjectUserResponse>> readProjectUsers(@PathVariable Long projectId){
        List<ProjectUserResponse> projects = projectService.getUserListByProject(projectId);
        return ResponseEntity.ok(projects);
    }
}
