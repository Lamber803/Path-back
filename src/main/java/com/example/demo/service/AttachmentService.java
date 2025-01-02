package com.example.demo.service;

import com.example.demo.model.entity.Attachment;
import com.example.demo.model.entity.Document;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AttachmentService {

    public static final String UPLOAD_DIR = "/Users/crystlawang/eclipse-workspace/pathfinder/pathfinder-backend/uploads";

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    // 上傳文件，並保存附件資料
    public Attachment uploadFile(MultipartFile file, Document document) throws IOException {
        // 文件保存目標路徑
        Path uploadDirectory = Paths.get(UPLOAD_DIR);

        // 檢查目錄是否存在
        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        // 獲取原始檔名
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            // 生成唯一檔名
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            Path targetLocation = uploadDirectory.resolve(uniqueFileName);

            // 保存文件到指定目錄
            Files.copy(file.getInputStream(), targetLocation);

            // 獲取檔案ＵＲＬ
            String fileUrl = "http://localhost:8080/api/files/uploads/" + uniqueFileName;

            // 獲取文件的 MIME類型
            String mimeType = Files.probeContentType(targetLocation);
            Attachment.FileType fileType = Attachment.FileType.FILE;  // 默认为 FILE 类型

            // 根據 MIME 類型設定文件類型
            if (mimeType != null) {
                if (mimeType.startsWith("image")) {
                    fileType = Attachment.FileType.IMAGE;
                } else if (mimeType.equals("application/pdf")) {
                    fileType = Attachment.FileType.PDF;
                } else if (mimeType.equals("application/msword")) {
                    fileType = Attachment.FileType.WORD;
                } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                    fileType = Attachment.FileType.EXCEL;
                } else if (mimeType.equals("text/plain")) {
                    fileType = Attachment.FileType.TEXT;
                }
            }

            // 創建并返回附件
            Attachment attachment = new Attachment();
            attachment.setFileName(fileName);
            attachment.setFileUrl(fileUrl);
            attachment.setFileSize(file.getSize());
            attachment.setFileType(fileType);  // 存儲文件類型
            attachment.setDocument(document);  // 關聯文檔

            // 保存附件到數據庫
            attachmentRepository.save(attachment);

            return attachment;
        } else {
            throw new IOException("文件名無效");
        }
    }


    // 文件下載功能
    public ResponseEntity<Resource> serveFile(String fileName) {
        Path file = Paths.get(UPLOAD_DIR).resolve(fileName);
        Resource resource = new FileSystemResource(file);

        // 文件存在且可讀取
        if (resource.exists() && resource.isReadable()) {
            try {
                String mimeType = Files.probeContentType(file);
                if (mimeType == null) {
                    mimeType = "application/octet-stream";  // 默認 MIME 類型
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(mimeType))
                        .body(resource);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 文件未找到
        }
    }
}
