package com.welcommu.moduleapi.file;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.file.service.FileService;
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

    private final FileService fileService;

    @PostMapping("posts/{postId}/files")
    public ResponseEntity<ApiResponse> createPostFile(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file) throws IOException {
        fileService.createPostFile(projectId, file);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트가 생성되었습니다."));
    }

    @PostMapping("checklists/{checklistId}/files")
    public ResponseEntity<ApiResponse> createChecklistFile(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file) throws IOException {
        fileService.createChecklistFile(projectId, file);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트가 생성되었습니다."));
    }
/*
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId) throws IOException {
        FileDownloadDto fileDto = fileService.downloadFile(projectId, postId, fileId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileDto.getOriginalFileName());

        return new ResponseEntity<>(fileDto.getFileData(), headers, HttpStatus.OK);
    }

 */

    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse> deleteFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId) {
        fileService.deleteFile(projectId, fileId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }
}
