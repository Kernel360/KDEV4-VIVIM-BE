package com.welcommu.moduleapi.link;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.link.LinkService;
import com.welcommu.moduleservice.link.dto.LinkListResponse;
import com.welcommu.moduleservice.link.dto.LinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "링크 API", description = "링크를 생성, 전체조회, 수정, 삭제할 수 있습니다.")
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/posts/{postId}/link")
    @Operation(summary = "게시글에 링크 생성")
    public ResponseEntity<ApiResponse> createPostLink(@PathVariable Long postId,
        @RequestBody LinkRequest request) {
        linkService.createPostLink(postId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "링크가 등록되었습니다."));
    }

    @PostMapping("/approvals/{approvalId}/links")
    @Operation(summary = "승인요청에 링크 생성")
    public ResponseEntity<ApiResponse> createApprovalLink(@PathVariable Long approvalId,
        @RequestBody LinkRequest request) {
        linkService.createApprovalLink(approvalId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "링크가 등록되었습니다."));
    }

    @PostMapping("/decisions/{decisionId}/links")
    @Operation(summary = "승인응답에 링크 생성")
    public ResponseEntity<ApiResponse> createDecisionLink(@PathVariable Long decisionId,
        @RequestBody LinkRequest request) {
        linkService.createDecisionLink(decisionId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "링크가 등록되었습니다."));
    }

    @GetMapping("/posts/{postId}/links")
    @Operation(summary = "게시글의 링크 전체 조회")
    public ResponseEntity<List<LinkListResponse>> getPostLinks(@PathVariable Long postId) {
        return ResponseEntity.ok(linkService.getPostLinks(postId));
    }

    @GetMapping("/approvals/{approvalId}/links")
    @Operation(summary = "승인요청의 링크 전체 조회")
    public ResponseEntity<List<LinkListResponse>> getApprovalLinks(@PathVariable Long approvalId) {
        return ResponseEntity.ok(linkService.getApprovalLinks(approvalId));
    }

    @GetMapping("/decisions/{decisionId}/links")
    @Operation(summary = "승인요청의 링크 전체 조회")
    public ResponseEntity<List<LinkListResponse>> getDecisionLinks(@PathVariable Long decisionId) {
        return ResponseEntity.ok(linkService.getDecisionLinks(decisionId));
    }

    @PatchMapping("/links/{linkId}")
    @Operation(summary = "링크 삭제")
    public ResponseEntity<ApiResponse> deleteLink(@PathVariable Long linkId) {
        linkService.deleteLink(linkId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "링크가 삭제되었습니다."));
    }
}