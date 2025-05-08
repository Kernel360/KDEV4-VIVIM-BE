package com.welcommu.moduleservice.projectpost;

import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentRequest;
import java.util.List;

public interface ProjectPostCommentService {

    void createComment(User user, Long postId, ProjectPostCommentRequest request, String clientIp)
        throws CustomException;

    void modifyComment(User user, Long postId, Long commentId, ProjectPostCommentRequest request)
        throws CustomException;

    List<ProjectPostCommentListResponse> getCommentList(Long projectPostId);

    void deleteComment(User user, Long postId, Long commentId) throws CustomException;
}