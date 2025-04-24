package com.welcommu.moduleapi.file;

import static com.welcommu.modulecommon.util.FileUtil.getExtensionFromContentType;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.file.File;
import com.welcommu.moduleservice.file.FileService;
import com.welcommu.moduleservice.file.dto.FileDownloadUrlResponse;
import com.welcommu.moduleservice.file.dto.FileListResponse;
import com.welcommu.moduleservice.file.dto.FileMetadataRequest;
import com.welcommu.moduleservice.file.dto.PreSignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final AmazonS3 amazonS3Client;
    private final FileService fileService;
    private String bucketName;
    private String today;
    private String uuid;
    private String extension;
    private String objectKey;
    private String fileName;
    private ObjectMetadata metadata;


    @PostMapping(path = "/posts/{postId}/file/presigned", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시물에 파일 업로드용 PreSigned URL 생성")
    public ResponseEntity<PreSignedUrlResponse> createPostFilePresignedUrl(
        @PathVariable Long postId,
        @RequestBody FileMetadataRequest fileMetadata) {

        // S3 경로 설정
        String bucketName = "vivim-s3";
        String today = LocalDate.now().toString();
        String uuid = UUID.randomUUID().toString();
        String extension = getExtensionFromContentType(fileMetadata.getContentType());
        String objectKey = "uploads/" + today + "/" + uuid + extension;

        // PreSigned URL 생성을 위한 메타데이터 설정
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
            bucketName, objectKey)
            .withMethod(HttpMethod.PUT)
            .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)); // 15분 유효

        // Content-Type 설정
        generatePresignedUrlRequest.addRequestParameter(
            Headers.CONTENT_TYPE, fileMetadata.getContentType());

        // PreSigned URL 생성
        URL preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();

        // DB에 파일 정보 저장 (실제 업로드는 클라이언트가 URL을 통해 직접 수행)
        fileService.createPostFile(fileMetadata.getFileName(), fileUrl, fileMetadata.getFileSize(),
            postId);

        // 클라이언트에게 PreSigned URL 반환
        PreSignedUrlResponse response = new PreSignedUrlResponse(
            preSignedUrl.toString(),
            fileUrl,
            objectKey
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{postId}/files")
    @Operation(summary = "게시글에 파일 목록 조회")
    public ResponseEntity<List<FileListResponse>> getPostFiles(@PathVariable Long postId) {
        return ResponseEntity.ok(fileService.getPostFiles(postId));
    }

    @GetMapping("/approvals/{approvalId}/files")
    @Operation(summary = "승인요청에 파일 목록 조회")
    public ResponseEntity<List<FileListResponse>> getApprovalFiles(@PathVariable Long approvalId) {
        return ResponseEntity.ok(fileService.getApprovalFiles(approvalId));
    }

    @GetMapping("/files/{fileId}/download")
    @Operation(summary = "파일 다운로드용 PreSigned URL 생성")
    public ResponseEntity<FileDownloadUrlResponse> getFileDownloadUrl(
        @PathVariable Long fileId) {
        // DB에서 파일 정보 조회
        File file = fileService.getFileInfo(fileId);

        // S3 객체 키 추출 (URL에서 추출하거나 DB에 별도 저장된 경우 그대로 사용)
        String objectKey = file.getFileUrl().substring(file.getFileUrl().indexOf("uploads"));
        String bucketName = "vivim-s3";

        // 다운로드용 PreSigned URL 생성 (유효시간 15분)
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15));

        // 파일명 지정을 위한 응답 헤더 설정
        ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides();
        String encodedFileName = UriUtils.encode(file.getFileName(), StandardCharsets.UTF_8);
        headerOverrides.setContentDisposition("attachment; filename=\"" + encodedFileName + "\"");
        generatePresignedUrlRequest.setResponseHeaders(headerOverrides);

        // PreSigned URL 생성
        URL preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        // 응답 생성
        FileDownloadUrlResponse response = new FileDownloadUrlResponse(
            preSignedUrl.toString(),
            file.getFileName(),
            file.getFileSize()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/files/{fileId}")
    @Operation(summary = "파일 삭제(SoftDelete)")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "파일이 삭제되었습니다."));
    }


}
