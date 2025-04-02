package com.welcommu.moduleapi.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.file.File;
import com.welcommu.moduleservice.file.FileService;
import com.welcommu.moduleservice.file.dto.FileListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.welcommu.modulecommon.util.FileUtil.getExtensionFromContentType;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
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

    @PostMapping(path = "/posts/{postId}/file/stream", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse>  createPostFile(@PathVariable Long postId, @RequestParam("file") MultipartFile file) throws IOException {
        metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());


        bucketName = "vivim-s3";
        today = LocalDate.now().toString();
        uuid = UUID.randomUUID().toString();
        extension = getExtensionFromContentType(file.getContentType());
        objectKey = "uploads/" + today + "/" + uuid + extension;
        fileName=file.getOriginalFilename();

        amazonS3Client.putObject(bucketName, objectKey, file.getInputStream(), metadata);
        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();

        fileService.createPostFile(fileName, fileUrl, file.getSize(), postId);

        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "파일이 업로드되었습니다."));
    }

    @PostMapping(path = "/approvals/{approvalId}/file/stream", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse>  createApprovalFile(@PathVariable Long approvalId, @RequestParam("file") MultipartFile file) throws IOException {
        metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());


        bucketName = "vivim-s3";
        today = LocalDate.now().toString();
        uuid = UUID.randomUUID().toString();
        extension = getExtensionFromContentType(file.getContentType());
        objectKey = "uploads/" + today + "/" + uuid + extension;
        fileName=file.getOriginalFilename();

        amazonS3Client.putObject(bucketName, objectKey, file.getInputStream(), metadata);
        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();

        fileService.createApprovalFile(fileName, fileUrl, file.getSize(), approvalId);

        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "파일이 완료되었습니다."));
    }

    @GetMapping("/posts/{postId}/files")
    public  ResponseEntity<List<FileListResponse>>  getPostFiles(@PathVariable Long postId) {
        return ResponseEntity.ok(fileService.getPostFiles(postId));
    }

    @GetMapping("/approvals/{approvalId}/files")
    public  ResponseEntity<List<FileListResponse>>  getApprovalFiles(@PathVariable Long approvalId) {
        return ResponseEntity.ok(fileService.getApprovalFiles(approvalId));
    }

    @GetMapping("/files/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        File file = fileService.getFileInfo(fileId); // DB에서 파일 정보 조회

        String objectKey = file.getFileUrl().substring(file.getFileUrl().indexOf("uploads"));
        S3Object s3Object = amazonS3Client.getObject("vivim-s3", objectKey);
        InputStreamResource resource = new InputStreamResource(s3Object.getObjectContent());

        String encodedFileName = UriUtils.encode(file.getFileName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.getFileSize())
                .body(resource);
    }
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "파일이 삭제되었습니다."));
    }





}
