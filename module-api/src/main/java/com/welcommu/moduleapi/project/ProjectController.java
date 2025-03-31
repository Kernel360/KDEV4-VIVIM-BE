package com.welcommu.moduleapi.project;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.CustomUserDetails;
import com.welcommu.moduleservice.project.ProjectService;
import com.welcommu.moduleservice.project.dto.*;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "프로젝트 API", description = "프로젝트를 셍성, 수정, 삭제시킬 수 있습니다.")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse> createProject(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody ProjectCreateRequest dto
    ) {

        //projectService.createProject(userDetails.getUser(), dto);
        projectService.createProject(dto);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트가 생성되었습니다."));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Optional<Project>> readProject(@PathVariable Long projectId
    ) {
        Optional<Project> project = projectService.readProject(projectId);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse> updateProject(
        @PathVariable Long projectId,
        @RequestBody ProjectUpdateRequest dto
    ) {
        projectService.modifyProject(projectId, dto);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 수정되었습니다."));
    }

    @GetMapping()
    public ResponseEntity<List<ProjectUserSummaryResponse>> readProjects(@RequestParam Long userId) {
       List<ProjectUserSummaryResponse> projects = projectService.readProjectsByUser(userId);
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

    @GetMapping("/all")
    public ResponseEntity<List<ProjectAdminSummaryResponse>> readAllProjectsForAdmin(){
        List<ProjectAdminSummaryResponse> projects = projectService.readProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<ProjectUserListResponse>> readProjectUsers(@PathVariable Long projectId){
        List<ProjectUserListResponse> projects = projectService.readUserListByProject(projectId);
        return ResponseEntity.ok(projects);
    }




}
