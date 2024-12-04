package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class ImageController {

    // 從 application.properties 中讀取 upload.dir 路徑
    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            // 檢查文件是否為空
            if (image.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No image file uploaded");
            }

            // 創建上傳目錄（如果不存在）
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一的文件名
            String fileName = UUID.randomUUID().toString() + "." + getFileExtension(image.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName);

            // 保存文件到指定的路徑
            image.transferTo(filePath.toFile());

            // 生成圖片的 URL，這裡假設使用的是本地開發環境
            String imageUrl = "http://localhost:8080/images/" + fileName;

            // 返回圖片 URL
            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
        }
    }

    // 取得檔案副檔名
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
