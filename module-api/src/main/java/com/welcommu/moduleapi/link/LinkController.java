package com.welcommu.moduleapi.link;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.link.LinkService;
import com.welcommu.moduleservice.link.dto.LinkRequest;
import com.welcommu.moduleservice.link.dto.LinkListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/posts/{postId}/link")
    public ResponseEntity<ApiResponse> createPostLink(@PathVariable Long postId, @RequestBody LinkRequest request) {
        linkService.createPostLink(postId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "링크가 등록되었습니다."));
    }

    @PostMapping("/approvals/{approvalId}/links")
    public ResponseEntity<ApiResponse> createApprovalLink(@PathVariable Long approvalId, @RequestBody LinkRequest request) {
        linkService.createApprovalLink(approvalId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "링크가 등록되었습니다."));
    }

    @GetMapping("/posts/{postId}/links")
    public ResponseEntity<List<LinkListResponse>> getPostLinks(@PathVariable Long postId) {
        return ResponseEntity.ok(linkService.getPostLinks(postId));
    }

    @GetMapping("/approvals/{approvalId}/links")
    public ResponseEntity<List<LinkListResponse>> getArppvoalLinks(@PathVariable Long approvalId) {
        return ResponseEntity.ok(linkService.getApprovalLinks(approvalId));
    }

    @DeleteMapping("/links/{linkId}")
    public ResponseEntity<ApiResponse> deleteLink(@PathVariable Long linkId) {
        linkService.deleteLink(linkId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "링크가 삭제되었습니다."));
    }
}