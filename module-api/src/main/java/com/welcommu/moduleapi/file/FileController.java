package com.welcommu.moduleapi.file;

import static com.welcommu.modulecommon.util.FileUtil.getExtensionFromContentType;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduleservice.file.FileService;
import com.welcommu.moduleservice.file.dto.CompleteUploadRequest;
import com.welcommu.moduleservice.file.dto.FileDownloadUrlResponse;
import com.welcommu.moduleservice.file.dto.FileListResponse;
import com.welcommu.moduleservice.file.dto.MultipartFileMetadataRequest;
import com.welcommu.moduleservice.file.dto.MultipartPresignedUrlResponse;
import com.welcommu.moduleservice.file.dto.PresignedPart;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "파일 API", description = "파일을 생성, 전체 조회, 삭제, 업로드, 다운로드할 수 있습니다.")
public class FileController {

//    private final AmazonS3 amazonS3Client;
//    private final FileService fileService;
//    private final String bucketName = "vivim-s3";
//    private static final long PART_SIZE = 25L * 1000 * 1000;
//    private static final long URL_EXPIRATION_MS = 1000L * 60 * 15;
//
//    @PostMapping(path = "/posts/{postId}/file/multipart", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "대용량 파일 업로드용 Multipart PreSigned URL 생성")
//    public ResponseEntity<MultipartPresignedUrlResponse> createPostFileMultipart(
//        @PathVariable Long postId,
//        @RequestBody MultipartFileMetadataRequest request) {
//        return ResponseEntity.ok(
//            createMultipartUrls(request,
//                key -> fileService.createMultipartFile(request, ReferenceType.POST, postId, key))
//        );
//    }
//
//    @PostMapping(path = "/approvals/{approvalId}/file/multipart", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "승인 요청에 대용량 파일 업로드용 Multipart PreSigned URL 생성")
//    public ResponseEntity<MultipartPresignedUrlResponse> createApprovalFileMultipart(
//        @PathVariable Long approvalId,
//        @RequestBody MultipartFileMetadataRequest request) {
//        return ResponseEntity.ok(
//            createMultipartUrls(request,
//                key -> fileService.createMultipartFile(request, ReferenceType.APPROVAL, approvalId,
//                    key))
//        );
//    }
//
//    @PostMapping(path = "/decisions/{decisionId}/file/multipart", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "승인 응답에 대용량 파일 업로드용 Multipart PreSigned URL 생성")
//    public ResponseEntity<MultipartPresignedUrlResponse> createDecisionFileMultipart(
//        @PathVariable Long decisionId,
//        @RequestBody MultipartFileMetadataRequest request) {
//        return ResponseEntity.ok(
//            createMultipartUrls(request,
//                key -> fileService.createMultipartFile(request, ReferenceType.DECISION, decisionId,
//                    key))
//        );
//    }
//
//    @PostMapping("/upload/complete")
//    @Operation(summary = "Multi-part 모아서 파일 업로드 ")
//    public void completeUpload(@RequestBody CompleteUploadRequest request) {
//        List<PartETag> etags = request.getParts().stream()
//            .map(p -> new PartETag(p.getPartNumber(), p.getEtag()))
//            .collect(Collectors.toList());
//
//        CompleteMultipartUploadRequest completeReq = new CompleteMultipartUploadRequest(
//            bucketName,
//            request.getKey(),
//            request.getUploadId(),
//            etags
//        );
//        amazonS3Client.completeMultipartUpload(completeReq);
//    }
//
//    @GetMapping("/posts/{postId}/files")
//    @Operation(summary = "게시글에 파일 목록 조회")
//    public ResponseEntity<List<FileListResponse>> getPostFiles(@PathVariable Long postId) {
//        return ResponseEntity.ok(
//            fileService.getFilesByReference(ReferenceType.POST, postId)
//        );
//    }
//
//    @GetMapping("/approvals/{approvalId}/files")
//    @Operation(summary = "승인요청에 파일 목록 조회")
//    public ResponseEntity<List<FileListResponse>> getApprovalFiles(@PathVariable Long approvalId) {
//        return ResponseEntity.ok(
//            fileService.getFilesByReference(ReferenceType.APPROVAL, approvalId)
//        );
//    }
//
//    @GetMapping("/decisions/{decisionId}/files")
//    @Operation(summary = "응답에 파일 목록 조회")
//    public ResponseEntity<List<FileListResponse>> getDecisionFiles(@PathVariable Long decisionId) {
//        return ResponseEntity.ok(
//            fileService.getFilesByReference(ReferenceType.DECISION, decisionId)
//        );
//    }
//
//    @GetMapping("/files/{fileId}/download")
//    @Operation(summary = "파일 다운로드용 PreSigned URL 생성")
//    public ResponseEntity<FileDownloadUrlResponse> downloadFile(@PathVariable Long fileId) {
//        File file = fileService.getFileInfo(fileId);
//        String presignedUrl = generateDownloadUrl(file);
//        FileDownloadUrlResponse response = FileDownloadUrlResponse.builder()
//            .preSignedUrl(presignedUrl)
//            .fileName(file.getFileName())
//            .fileSize(file.getFileSize())
//            .build();
//        return ResponseEntity.ok(response);
//    }
//
//    @PatchMapping("/files/{fileId}")
//    @Operation(summary = "파일 삭제(SoftDelete)")
//    public ResponseEntity<ApiResponse> deleteFile(@PathVariable Long fileId) {
//        fileService.deleteFile(fileId);
//        return ResponseEntity.ok(
//            new ApiResponse(HttpStatus.OK.value(), "파일이 삭제되었습니다.")
//        );
//    }
//
//    private MultipartPresignedUrlResponse createMultipartUrls(
//        MultipartFileMetadataRequest request,
//        Consumer<String> saveMeta) {
//
//        String objectKey = generateObjectKey(request.getContentType());
//        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();
//
//        long fileSize = request.getFileSize();
//        int partCount = (int) Math.ceil((double) fileSize / PART_SIZE);
//        InitiateMultipartUploadRequest initReq = new InitiateMultipartUploadRequest(bucketName,
//            objectKey)
//            .withObjectMetadata(new ObjectMetadata() {{
//                setContentType(request.getContentType());
//            }});
//        String uploadId = amazonS3Client.initiateMultipartUpload(initReq).getUploadId();
//
//        List<PresignedPart> parts = new ArrayList<>(partCount);
//        for (int i = 1; i <= partCount; i++) {
//            parts.add(new PresignedPart(i, generatePartUrl(objectKey, uploadId, i)));
//        }
//
//        saveMeta.accept(fileUrl);
//        return new MultipartPresignedUrlResponse(objectKey, uploadId, fileUrl, parts);
//    }
//
//    private String generatePartUrl(String key, String uploadId, int partNumber) {
//        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key,
//            HttpMethod.PUT)
//            .withExpiration(new Date(System.currentTimeMillis() + URL_EXPIRATION_MS));
//        req.addRequestParameter("uploadId", uploadId);
//        req.addRequestParameter("partNumber", Integer.toString(partNumber));
//        return amazonS3Client.generatePresignedUrl(req).toString();
//    }
//
//    private String generateObjectKey(String contentType) {
//        String date = LocalDate.now().toString();
//        String uuid = UUID.randomUUID().toString();
//        String ext = getExtensionFromContentType(contentType);
//        return String.format("uploads/%s/%s%s", date, uuid, ext);
//    }
//
//    private String generateDownloadUrl(File file) {
//        String objectKey = file.getFileUrl().substring(file.getFileUrl().indexOf("uploads"));
//        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectKey)
//            .withMethod(HttpMethod.GET)
//            .withExpiration(new Date(System.currentTimeMillis() + URL_EXPIRATION_MS));
//        com.amazonaws.services.s3.model.ResponseHeaderOverrides headers = new com.amazonaws.services.s3.model.ResponseHeaderOverrides();
//        String encodedName = UriUtils.encode(file.getFileName(), StandardCharsets.UTF_8);
//        headers.setContentDisposition("attachment; filename=\"" + encodedName + "\"");
//        req.setResponseHeaders(headers);
//        return amazonS3Client.generatePresignedUrl(req).toString();
//    }
}

