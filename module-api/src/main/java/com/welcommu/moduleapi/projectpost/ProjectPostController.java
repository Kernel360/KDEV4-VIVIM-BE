package com.welcommu.moduleapi.projectpost;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import com.welcommu.moduleservice.projectpost.ProjectPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/posts")
@RequiredArgsConstructor
public class ProjectPostController {
    private final ProjectPostService projectPostService;


    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@PathVariable Long projectId, @RequestBody ProjectPostRequest request) {
        projectPostService.createPost(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), "게시글이 생성되었습니다."));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse> modifyPost(@PathVariable Long projectId, @PathVariable Long postId, @RequestBody ProjectPostRequest request) {
        projectPostService.modifyPost(projectId, postId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 수정되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<ProjectPostListResponse>> getPost(@PathVariable Long projectId) {
        List<ProjectPostListResponse> resultList = projectPostService.getPostList(projectId);
        return ResponseEntity.ok(resultList);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long projectId, @PathVariable Long postId) {
        projectPostService.deletePost(projectId, postId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 삭제되었습니다."));
    }
}
