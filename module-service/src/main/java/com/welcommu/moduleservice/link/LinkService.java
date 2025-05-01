package com.welcommu.moduleservice.link;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduledomain.link.Link;
import com.welcommu.moduleinfra.link.LinkRepository;
import com.welcommu.moduleservice.link.audit.LinkAuditService;
import com.welcommu.moduleservice.link.dto.LinkListResponse;
import com.welcommu.moduleservice.link.dto.LinkRequest;
import com.welcommu.moduleservice.user.dto.UserSnapshot;
import java.util.List;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final LinkAuditService linkAuditService;

    public void createPostLink(Long postId, LinkRequest request, Long creatorId) {
        Link newLink = request.toEntity(request, ReferenceType.POST, postId);
        Link savedLink =  linkRepository.save(newLink);
        linkAuditService.createAuditLog(LinkListResponse.from(savedLink), creatorId);
    }

    public void createApprovalLink(Long approvalId, LinkRequest request, Long creatorId) {

        Link newLink = request.toEntity(request, ReferenceType.APPROVAL, approvalId);
        Link savedLink =  linkRepository.save(newLink);
        linkAuditService.createAuditLog(LinkListResponse.from(savedLink), creatorId);
    }

    public void createDecisionLink(Long decisionId, LinkRequest request, Long creatorId) {
        Link newLink = request.toEntity(request, ReferenceType.DECISION, decisionId);
        Link savedLink =  linkRepository.save(newLink);
        linkAuditService.createAuditLog(LinkListResponse.from(savedLink), creatorId);
    }

    public List<LinkListResponse> getPostLinks(Long postId) {
        List<Link> links = linkRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
            postId, ReferenceType.POST);
        return links.stream()
            .map(LinkListResponse::from)
            .collect(Collectors.toList());
    }

    public List<LinkListResponse> getApprovalLinks(Long approvalId) {
        List<Link> links = linkRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
            approvalId, ReferenceType.APPROVAL);
        return links.stream()
            .map(LinkListResponse::from)
            .collect(Collectors.toList());
    }

    public List<LinkListResponse> getDecisionLinks(Long decisionId) {
        List<Link> links = linkRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
            decisionId, ReferenceType.DECISION);
        return links.stream()
            .map(LinkListResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLink(Long linkId, Long deleterId) {
        Link existingLink = linkRepository.findById(linkId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_LINK));
        existingLink.setDeletedAt();
        linkAuditService.deleteAuditLog(LinkListResponse.from(existingLink), deleterId);
    }
}
