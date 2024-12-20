package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.FileUploadException;

@Service
public class AttachmentService {

    private final Path uploadDirectory = Paths.get("uploads");

    @Value("${file.upload.url.base:http://localhost:8080/uploads/}")
    private String fileBaseUrl;
@Async
    public String uploadFile(MultipartFile file) throws IOException {
        // 检查文件大小限制
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new FileUploadException("文件大小超出限制");
        }

        // 创建存储目录（如果不存在）
        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        // 获取原始文件名，并生成唯一的文件名
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadDirectory.resolve(fileName);

        // 保存文件
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 返回文件的公共 URL 地址
        return fileBaseUrl + fileName; // 基于文件保存路径构造完整的 URL
    }
}

