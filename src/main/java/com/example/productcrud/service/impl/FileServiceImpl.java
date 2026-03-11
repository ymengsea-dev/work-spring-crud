package com.example.productcrud.service.impl;

import com.example.productcrud.constraint.ExceptionCode;
import com.example.productcrud.exception.BusinessException;
import com.example.productcrud.model.FileMetaData;
import com.example.productcrud.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private boolean isAllowedExtension(String filename){
        return  filename.endsWith(".png")||
                filename.endsWith(".svg") ||
                filename.endsWith(".jpg") ||
                filename.endsWith(".jpeg")||
                filename.endsWith(".gif");
    }

    @Override
    @SneakyThrows
    public FileMetaData uploadFile(MultipartFile multipartFile) {
        String fileExtension = multipartFile.getOriginalFilename();
        if (fileExtension == null || !isAllowedExtension(fileExtension)){
            throw new BusinessException(ExceptionCode.FILE_not_SUPPORT);
        }

        // check there are bucket or create new
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // make file name
        String filename = multipartFile.getOriginalFilename() + UUID.randomUUID();

        // put object in file name
        minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                .build());

        // make image url
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/files/preview-file/"+filename).toUriString();

        //return File into
        return FileMetaData.builder()
                .fileName(filename)
                .fileSize(multipartFile.getSize())
                .fileType(multipartFile.getContentType())
                .fileUrl(fileUrl)
                .build();
    }

    @Override
    @SneakyThrows
    public InputStream getFileByName(String fileName){
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build()) ;
    }
}
