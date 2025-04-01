package com.welcommu.moduleapi.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.file.dto.FileCreateRequest;
import com.welcommu.moduleservice.file.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import static com.welcommu.modulecommon.util.FileUtil.getExtensionFromContentType;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final AmazonS3 amazonS3Client;
    private final FileService fileService;

    @PostMapping("/posts/{postId}/file/stream")
    public ResponseEntity<ApiResponse>  createPostFile(@PathVariable Long postId, @RequestParam("fileName") String fileName,HttpServletRequest request) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(request.getContentType());
        metadata.setContentLength(request.getContentLengthLong());


        String bucketName = "vivim-s3";
        String today = LocalDate.now().toString();
        String uuid = UUID.randomUUID().toString();
        String extension = getExtensionFromContentType(request.getContentType()); // 예: .jpg
        String objectKey = "uploads/" + today + "/" + uuid + extension;

        amazonS3Client.putObject(bucketName, objectKey, request.getInputStream(), metadata);
        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();

        fileService.createPostFile(fileName, fileUrl, request.getContentLengthLong(), postId);

        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "이미지 업로드가 완료되었습니다."));
    }
    @PostMapping("/approvals/{approvalId}/file/stream")
    public ResponseEntity<ApiResponse>  createApprovalFile(@PathVariable Long approvalId, @RequestParam("fileName") String fileName, HttpServletRequest request) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(request.getContentType());
        metadata.setContentLength(request.getContentLengthLong());

        String bucketName = "vivim-s3";
        String today = LocalDate.now().toString();
        String uuid = UUID.randomUUID().toString();
        String extension = getExtensionFromContentType(request.getContentType()); // 예: .jpg
        String objectKey = "uploads/" + today + "/" + uuid + extension;

        amazonS3Client.putObject(bucketName, objectKey, request.getInputStream(), metadata);
        String fileUrl = amazonS3Client.getUrl(bucketName, objectKey).toString();
        fileService.createApprovalFile(fileName,fileUrl, request.getContentLengthLong(), approvalId);

        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "이미지 업로드가 완료되었습니다."));
    }



}
