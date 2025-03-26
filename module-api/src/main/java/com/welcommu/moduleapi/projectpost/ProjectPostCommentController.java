package com.welcommu.moduleapi.projectpost;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ProjectPostCommentController {


    @PostMapping
    public Long createComment(@PathVariable Long postId, @RequestBody ProjectPostCommentRequest request) {
        //return commentService.createComment(postId, request);
        return 1L;
    }
}
