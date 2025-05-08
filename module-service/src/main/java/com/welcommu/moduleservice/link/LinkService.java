package com.welcommu.moduleservice.link;

import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduleservice.link.dto.LinkListResponse;
import com.welcommu.moduleservice.link.dto.LinkRequest;
import java.util.List;

public interface LinkService {

    void createPostLink(Long postId, LinkRequest request, Long creatorId) throws CustomException;

    void createApprovalLink(Long approvalId, LinkRequest request, Long creatorId)
        throws CustomException;

    void createDecisionLink(Long decisionId, LinkRequest request, Long creatorId)
        throws CustomException;

    List<LinkListResponse> getPostLinks(Long postId);

    List<LinkListResponse> getApprovalLinks(Long approvalId);

    List<LinkListResponse> getDecisionLinks(Long decisionId);

    void deleteLink(Long linkId, Long deleterId) throws CustomException;
}
