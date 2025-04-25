package com.welcommu.moduleservice.link;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduledomain.link.Link;
import com.welcommu.moduleinfra.link.LinkRepository;
import com.welcommu.moduleservice.link.dto.LinkListResponse;
import com.welcommu.moduleservice.link.dto.LinkRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;


    public void createPostLink(Long postId, LinkRequest request) {

        Link newLink = Link.createLink(request.getTitle(), request.getUrl(), ReferenceType.POST,
            postId);
        linkRepository.save(newLink);
    }

    public void createApprovalLink(Long approvalId, LinkRequest request) {

        Link newLink = Link.createLink(request.getTitle(), request.getUrl(), ReferenceType.APPROVAL,
            approvalId);
        linkRepository.save(newLink);
    }

    public List<LinkListResponse> getPostLinks(Long postId) {
        List<Link> links = linkRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
            postId, ReferenceType.POST);
        return links.stream()
            .map(LinkListResponse::from)
            .collect(Collectors.toList());
    }

    public List<LinkListResponse> getApprovalLinks(Long postId) {
        List<Link> links = linkRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
            postId, ReferenceType.APPROVAL);
        return links.stream()
            .map(LinkListResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLink(Long linkId) {
        Link existingLink = linkRepository.findById(linkId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_LINK));
        existingLink.setDeletedAt();
    }
}
