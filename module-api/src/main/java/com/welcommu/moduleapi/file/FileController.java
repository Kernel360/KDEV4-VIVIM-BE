package com.welcommu.moduleapi.file;

import com.welcommu.dto.ApiResponse;
import com.welcommu.moduleservice.file.dto.FileDownloadDto;
import com.welcommu.moduleservice.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/projects/{projectId}/posts/{postId}/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<ApiResponse> createFile(
            @PathVariable Long projectId,
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile file) throws IOException {
        fileService.createFile(projectId, postId, file);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트가 생성되었습니다."));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long projectId,
            @PathVariable Long postId,
            @PathVariable Long fileId) throws IOException {
        FileDownloadDto fileDto = fileService.downloadFile(projectId, postId, fileId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileDto.getOriginalFileName());

        return new ResponseEntity<>(fileDto.getFileData(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse> deleteFile(
            @PathVariable Long projectId,
            @PathVariable Long postId,
            @PathVariable Long fileId) {
        fileService.deleteFile(projectId, postId, fileId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }
}
