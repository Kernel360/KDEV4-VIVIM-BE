package com.welcommu.moduleapi.projectpost.controller;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.projectpost.dto.*;
import com.welcommu.moduleservice.projectpost.service.ProjectPostCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.welcommu.modulecommon.util.IpUtil.getClientIp;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class ProjectPostCommentController {
    private final ProjectPostCommentService projectPostCommentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createComment(@PathVariable Long postId, @RequestBody ProjectPostCommentRequest request, HttpServletRequest httpRequest){
        String clientIp = getClientIp(httpRequest);
        projectPostCommentService.createComment(postId,request, clientIp);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "댓글 생성 완료"));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse> modifyComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody ProjectPostCommentRequest request) {
        projectPostCommentService.modifyComment(postId, commentId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 수정되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<ProjectPostCommentListResponse>> getCommentList(@PathVariable Long postId) {
        List<ProjectPostCommentListResponse> resultList = projectPostCommentService.getCommentList(postId);
        return ResponseEntity.ok(resultList);
    }



    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        projectPostCommentService.deleteComment(postId, commentId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 삭제되었습니다."));
    }
}
