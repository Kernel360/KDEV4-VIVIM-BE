package com.welcommu.moduleapi.project;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.project.*;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.dto.ProjectDeleteRequest;
import com.welcommu.moduleservice.project.dto.ProjectSummaryResponse;
import com.welcommu.moduleservice.project.dto.ProjectUpdateRequest;
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

    /*
    * {
    * 아래와 같이 입력이 들어온다고 가정
          "name": "프로젝트명",
          "description": "설명",
          "startDate": "2025-04-01",
          "endDate": "2025-04-30",
          "clientManagers": [ { "userId": 1 }, { "userId": 2 } ],
          "clientUsers":    [ { "userId": 3 }, { "userId": 4 } ],
          "devManagers":    [ { "userId": 5 } ],
          "devUsers":       [ { "userId": 6 }, { "userId": 7 } ]
        }
    *
    * */
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
    public ResponseEntity<List<ProjectSummaryResponse>> readProjects(@RequestBody Long userId) {
       List<ProjectSummaryResponse> projects = projectService.readProjectsByUser(userId);
       return ResponseEntity.ok(projects);
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse> DeleteProject(@PathVariable Long projectId,@RequestBody ProjectDeleteRequest dto) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }
    // TODO
    // 모든 프로젝트 조회(관리자)





}
