package com.welcommu.moduleapi.projectpost;

import com.welcommu.dto.ApiResponse;
import com.welcommu.moduleservice.projectpost.ProjectPostCommentRequest;
import com.welcommu.moduleservice.projectpost.ProjectPostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class ProjectPostCommentController {
    private final ProjectPostCommentService projectPostCommentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createComment(@PathVariable Long postId, @RequestBody ProjectPostCommentRequest request){
        projectPostCommentService.createComment(postId,request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "댓글 생성 완료"));
    }

}
