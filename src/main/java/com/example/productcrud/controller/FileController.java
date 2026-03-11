package com.example.productcrud.controller;

import com.example.productcrud.constraint.ResponseCode;
import com.example.productcrud.model.FileMetaData;
import com.example.productcrud.model.dto.reponse.ApiResponse;
import com.example.productcrud.model.dto.reponse.ApiStatus;
import com.example.productcrud.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Tag(name = "File Upload")
@RequestMapping("/api/v1/files")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @SneakyThrows
    @PostMapping(value = "/upload-file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileMetaData>> uploadFile(@RequestPart(value = "file", required = true)MultipartFile file){
        FileMetaData uploadFile = fileService.uploadFile(file);
        ApiResponse<FileMetaData> body = ApiResponse.<FileMetaData>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.FILE_UPLOAD.name())
                        .message(ResponseCode.FILE_UPLOAD.getMessage())
                        .build())
                .data(FileMetaData.builder()
                        .fileUrl(uploadFile.getFileUrl())
                        .fileName(uploadFile.getFileName())
                        .fileSize(uploadFile.getFileSize())
                        .fileType(uploadFile.getFileType())
                        .build())
                .build();
        return ResponseEntity.ok(body);
    }

    @SneakyThrows
    @GetMapping("/preview-file/{file-name}")
    public ResponseEntity<?> getFileByName(@PathVariable("file-name") String fileName){
        InputStream resource = fileService.getFileByName(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource.readAllBytes());
    }

}
