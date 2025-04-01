package com.welcommu.moduleapi.file;

import com.welcommu.modulecommon.config.S3Config;
import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.file.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final S3Config amazonS3Client;


    /*

    @PostMapping("/file/stream")
    public ResponseEntity<ApiResponse>  createPostFile(HttpServletRequest request) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(request.getContentType());
        metadata.setContentLength(request.getContentLengthLong());

        String bucketName = "bucketName";
        String objectKey = "objectKey";

        amazonS3Client.putObject(bucketName, objectKey, request.getInputStream(), metadata);

        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "이미지 업로드가 완료되었습니다."));
    }




    @PostMapping("checklists/{checklistId}/files")
    public ResponseEntity<ApiResponse> createChecklistFile(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file) throws IOException {
        fileService.createChecklistFile(projectId, file);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트가 생성되었습니다."));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse> deleteFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId) {
        fileService.deleteFile(projectId, fileId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }

     */
}
