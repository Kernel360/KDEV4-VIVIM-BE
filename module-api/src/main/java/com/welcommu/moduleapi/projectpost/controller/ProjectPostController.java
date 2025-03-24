package com.welcommu.moduleapi.projectpost.controller;

import com.welcommu.moduleapi.projectpost.dto.ProjectPostRequest;
import com.welcommu.moduleservice.projectpost.dto.CreateProjectPostCommand;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResult;
import com.welcommu.moduleservice.projectpost.service.ProjectPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/projects/{projectId}/posts")
@RequiredArgsConstructor
public class ProjectPostController {
    private final ProjectPostService projectPostService;



    @PostMapping
    public Long createPost(@PathVariable Long projectId, @RequestBody ProjectPostRequest request) {
        CreateProjectPostCommand command = new CreateProjectPostCommand(projectId, request.getTitle(), request.getContent(), request.getProjectPostStatus());
        return projectPostService.createPost(projectId, command);
    }

    @PutMapping("/{postId}")
    public Long modifyPost(@PathVariable Long projectId, @PathVariable Long postId, @RequestBody ProjectPostRequest request) {
        CreateProjectPostCommand command = new CreateProjectPostCommand(projectId, request.getTitle(), request.getContent(), request.getProjectPostStatus());
        return projectPostService.modifyPost(projectId, postId, command);
    }

    @GetMapping
    public ResponseEntity<List<ProjectPostListResult>> getPost(@PathVariable Long projectId) {
        List<ProjectPostListResult> resultList = projectPostService.getPostList(projectId);
        return ResponseEntity.ok(resultList);
    }

    @DeleteMapping("/{postId}")
    public Long deletePost(@PathVariable Long projectId, @PathVariable Long postId) {
        return projectPostService.deletePost(postId);
    }
}
