package com.welcommu.moduleapi.file;

import static com.welcommu.modulecommon.util.FileUtil.getExtensionFromContentType;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.file.File;
import com.welcommu.moduleservice.file.FileService;
import com.welcommu.moduleservice.file.dto.CompleteUploadRequest;
import com.welcommu.moduleservice.file.dto.FileDownloadUrlResponse;
import com.welcommu.moduleservice.file.dto.FileListResponse;
import com.welcommu.moduleservice.file.dto.MultipartFileMetadataRequest;
import com.welcommu.moduleservice.file.dto.MultipartPresignedUrlResponse;
import com.welcommu.moduleservice.file.dto.PresignedPart;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    private final AmazonS3 amazonS3Client;
    private final FileService fileService;
    private final String bucketName = "vivim-s3";


    @PostMapping(path = "/posts/{postId}/file/multipart", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "대용량 파일 업로드용 Multipart PreSigned URL 생성")
    public ResponseEntity<MultipartPresignedUrlResponse> createMultipartPresignedUrls(
        @PathVariable Long postId,
        @RequestBody MultipartFileMetadataRequest request) {

        String objectKey = generateObjectKey(request.getContentType());
        long fileSize = request.getFileSize();
        long partSize = 25 * 1000 * 1000L; // 각 파트는 25MB로 설정
        long partCount = (long) (Math.ceil((double) fileSize / partSize));

        //initiateRequest를 통해 조각으로 업로드된 파일들이 합쳐짐
        InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest(
            bucketName, objectKey)
            .withObjectMetadata(new ObjectMetadata() {{
                setContentType(request.getContentType());
            }});
        InitiateMultipartUploadResult initResponse = amazonS3Client.initiateMultipartUpload(
            initiateRequest);

        String uploadId = initResponse.getUploadId(); //uploadId를 통해 나눠서 업로드된 파일들이 합쳐질 수 있음.
        List<PresignedPart> presignedParts = new ArrayList<>();
        for (int i = 1; i <= partCount; i++) {
            GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(
                bucketName, objectKey, HttpMethod.PUT)
                .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15));
            presignedUrlRequest.addRequestParameter("partNumber", String.valueOf(i));
            presignedUrlRequest.addRequestParameter("uploadId", uploadId);
            URL presignedUrl = amazonS3Client.generatePresignedUrl(presignedUrlRequest);
            presignedParts.add(new PresignedPart(i, presignedUrl.toString()));
        }

        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();

        fileService.createMultipartPostFile(request, postId, fileUrl);
        MultipartPresignedUrlResponse response = new MultipartPresignedUrlResponse(
            objectKey, uploadId, fileUrl, presignedParts
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload/complete")
    @Operation(summary = "Multi-part 모아서 파일 업로드 ")
    public void completeUpload(@RequestBody CompleteUploadRequest request) {
        List<PartETag> etags = request.getParts().stream()
            .map(p -> new PartETag(p.getPartNumber(), p.getEtag()))
            .collect(Collectors.toList());

        CompleteMultipartUploadRequest completeReq = new CompleteMultipartUploadRequest(
            bucketName,
            request.getKey(),
            request.getUploadId(),
            etags
        );
        System.out.print("Region = ");
        System.out.println(amazonS3Client.getRegion());
        amazonS3Client.completeMultipartUpload(completeReq);
    }

    @PostMapping(path = "/approvals/{approvalId}/file/multipart", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "승인 요청에 대용량 파일 업로드용 Multipart PreSigned URL 생성")
    public ResponseEntity<MultipartPresignedUrlResponse> createApprovalFileMultipartUrls(
        @PathVariable Long approvalId,
        @RequestBody MultipartFileMetadataRequest request) {

        String objectKey = generateObjectKey(request.getContentType());
        long fileSize = request.getFileSize();
        long partSize = 25 * 1000 * 1000L; // 각 파트 25MB
        long partCount = (long) Math.ceil((double) fileSize / partSize);

        // 1. 멀티파트 업로드 세션 시작
        InitiateMultipartUploadRequest initReq = new InitiateMultipartUploadRequest(bucketName,
            objectKey)
            .withObjectMetadata(new ObjectMetadata() {{
                setContentType(request.getContentType());
            }});
        InitiateMultipartUploadResult initRes = amazonS3Client.initiateMultipartUpload(initReq);
        String uploadId = initRes.getUploadId();

        // 2. 파트별 PreSigned URL 생성
        List<PresignedPart> parts = new ArrayList<>();
        for (int i = 1; i <= partCount; i++) {
            GeneratePresignedUrlRequest presignedReq = new GeneratePresignedUrlRequest(bucketName,
                objectKey, HttpMethod.PUT)
                .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15));
            presignedReq.addRequestParameter("uploadId", uploadId);
            presignedReq.addRequestParameter("partNumber", String.valueOf(i));
            URL url = amazonS3Client.generatePresignedUrl(presignedReq);
            parts.add(new PresignedPart(i, url.toString()));
        }

        // 3. 파일 메타 정보 저장 (DB 등)
        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();
        fileService.createMultipartApprovalFile(request, approvalId, fileUrl);

        // 4. 응답 반환
        MultipartPresignedUrlResponse response = new MultipartPresignedUrlResponse(
            objectKey, uploadId, fileUrl, parts
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/decisions/{decisionId}/file/multipart", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "승인 응답에 대용량 파일 업로드용 Multipart PreSigned URL 생성")
    public ResponseEntity<MultipartPresignedUrlResponse> createDecisionFileMultipartUrls(
        @PathVariable Long decisionId,
        @RequestBody MultipartFileMetadataRequest request) {

        String objectKey = generateObjectKey(request.getContentType());
        long fileSize = request.getFileSize();
        long partSize = 25 * 1000 * 1000L; // 각 파트 25MB
        long partCount = (long) Math.ceil((double) fileSize / partSize);

        // 1. 멀티파트 업로드 세션 시작
        InitiateMultipartUploadRequest initReq = new InitiateMultipartUploadRequest(bucketName,
            objectKey)
            .withObjectMetadata(new ObjectMetadata() {{
                setContentType(request.getContentType());
            }});
        InitiateMultipartUploadResult initRes = amazonS3Client.initiateMultipartUpload(initReq);
        String uploadId = initRes.getUploadId();

        // 2. 파트별 PreSigned URL 생성
        List<PresignedPart> parts = new ArrayList<>();
        for (int i = 1; i <= partCount; i++) {
            GeneratePresignedUrlRequest presignedReq = new GeneratePresignedUrlRequest(bucketName,
                objectKey, HttpMethod.PUT)
                .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15));
            presignedReq.addRequestParameter("uploadId", uploadId);
            presignedReq.addRequestParameter("partNumber", String.valueOf(i));
            URL url = amazonS3Client.generatePresignedUrl(presignedReq);
            parts.add(new PresignedPart(i, url.toString()));
        }

        // 3. 파일 메타 정보 저장 (DB 등)
        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();
        fileService.createMultipartDecisionFile(request, decisionId, fileUrl);

        // 4. 응답 반환
        MultipartPresignedUrlResponse response = new MultipartPresignedUrlResponse(
            objectKey, uploadId, fileUrl, parts
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

    @GetMapping("/decisions/{decisionId}/files")
    @Operation(summary = "게시글에 파일 목록 조회")
    public ResponseEntity<List<FileListResponse>> getDecisionFiles(@PathVariable Long decisionId) {
        return ResponseEntity.ok(fileService.getDecisionFiles(decisionId));
    }

    @GetMapping("/files/{fileId}/download")
    @Operation(summary = "파일 다운로드용 PreSigned URL 생성")
    public ResponseEntity<FileDownloadUrlResponse> getFileDownloadUrl(
        @PathVariable Long fileId) {

        File file = fileService.getFileInfo(fileId);

        String objectKey = file.getFileUrl().substring(file.getFileUrl().indexOf("uploads"));

        // 다운로드용 PreSigned URL관련 설정(유효시간, HttpMethod등)
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

        FileDownloadUrlResponse response = new FileDownloadUrlResponse(
            preSignedUrl.toString(),
            file.getFileName(),
            file.getFileSize()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/files/{fileId}")
    @Operation(summary = "파일 삭제(SoftDelete)")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "파일이 삭제되었습니다."));
    }

    private String generateObjectKey(String contentType) {
        String today = LocalDate.now().toString();
        String uuid = UUID.randomUUID().toString();
        String extension = getExtensionFromContentType(contentType);
        return "uploads/" + today + "/" + uuid + extension;
    }
}
