package com.example.productcrud.model;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class FileMetaData {
    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize;
}
