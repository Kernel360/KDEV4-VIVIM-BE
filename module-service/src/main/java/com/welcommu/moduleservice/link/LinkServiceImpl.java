package com.welcommu.moduleservice.link;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduledomain.link.Link;
import com.welcommu.moduleinfra.link.LinkRepository;
import com.welcommu.moduleservice.link.audit.LinkAuditService;
import com.welcommu.moduleservice.link.dto.LinkListResponse;
import com.welcommu.moduleservice.link.dto.LinkRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final LinkAuditService linkAuditService;

    @Override
    @Transactional
    public void createPostLink(Long postId, LinkRequest request, Long creatorId)
        throws CustomException {
        Link newLink = request.toEntity(request, ReferenceType.POST, postId);
        Link saved = linkRepository.save(newLink);
        linkAuditService.createAuditLog(LinkListResponse.from(saved), creatorId);
    }

    @Override
    @Transactional
    public void createApprovalLink(Long approvalId, LinkRequest request, Long creatorId)
        throws CustomException {
        Link newLink = request.toEntity(request, ReferenceType.APPROVAL, approvalId);
        Link saved = linkRepository.save(newLink);
        linkAuditService.createAuditLog(LinkListResponse.from(saved), creatorId);
    }

    @Override
    @Transactional
    public void createDecisionLink(Long decisionId, LinkRequest request, Long creatorId)
        throws CustomException {
        Link newLink = request.toEntity(request, ReferenceType.DECISION, decisionId);
        Link saved = linkRepository.save(newLink);
        linkAuditService.createAuditLog(LinkListResponse.from(saved), creatorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkListResponse> getPostLinks(Long postId) {
        return linkRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(postId,
                ReferenceType.POST)
            .stream()
            .map(LinkListResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkListResponse> getApprovalLinks(Long approvalId) {
        return linkRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(approvalId,
                ReferenceType.APPROVAL)
            .stream()
            .map(LinkListResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkListResponse> getDecisionLinks(Long decisionId) {
        return linkRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(decisionId,
                ReferenceType.DECISION)
            .stream()
            .map(LinkListResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteLink(Long linkId, Long deleterId) throws CustomException {
        Link existing = linkRepository.findById(linkId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_LINK));
        existing.setDeletedAt();
        linkAuditService.deleteAuditLog(LinkListResponse.from(existing), deleterId);
    }
}