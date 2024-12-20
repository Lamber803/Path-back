package com.example.demo.service;

import com.example.demo.model.dto.CreateDocumentDTO;
import com.example.demo.model.dto.UpdateDocumentDTO;
import com.example.demo.model.entity.Document;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.Attachment;
import com.example.demo.model.entity.Content;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.repository.ContentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ContentRepository contentRepository;
    
    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
 // 判断文件类型的方法
    private Attachment.FileType determineFileType(MultipartFile file) {
        // 获取文件的 MIME 类型
        String contentType = file.getContentType();

        // 如果是图像文件，返回 IMAGE 类型
        if (contentType != null && contentType.startsWith("image")) {
            return Attachment.FileType.IMAGE;
        }

        // 否则，返回 FILE 类型
        return Attachment.FileType.FILE;
    }



    @Autowired
    private UserRepository userRepository;  // 用於查找用戶

    // 創建新文檔
    public Document createDocument(Integer userId, CreateDocumentDTO createDocumentDTO, List<MultipartFile> files) {
        // 查找用户
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("找不到 ID 为 " + userId + " 的用户");
        }

        User user = userOptional.get();  // 获取 User 对象

        // 创建 Document 实体
        Document document = new Document();
        document.setUser(user);  // 设置 User 实体
        document.setTitle(createDocumentDTO.getTitle());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        // 获取 HTML 内容
        String htmlContent = createDocumentDTO.getHtmlContent();

        // 如果有文件上传，处理图片或文件 URL
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    // 上传文件并获取 URL
                    String fileUrl = attachmentService.uploadFile(file);  // 上传文件服务，返回可访问的 URL（HTTP/HTTPS）

                    // 处理 Blob URL 替换
                    htmlContent = htmlContent.replace("blob://", fileUrl);

                    // 创建附件并保存
                    Attachment attachment = new Attachment();
                    attachment.setDocument(document);
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(fileUrl);
                    attachment.setFileSize(file.getSize());
                    attachment.setFileType(determineFileType(file));  // 根据 MIME 类型设置文件类型

                    // 保存附件信息到数据库
                    attachmentRepository.save(attachment);

                } catch (IOException e) {
                    throw new RuntimeException("文件上传失败", e);
                }
            }
        }

        // 创建并保存文档内容
        Content content = new Content();
        content.setHtmlContent(htmlContent);
        content.setDocument(document);  // 将内容与文档关联
        document.setContent(content);    // 设置 Document 的 Content

        // 保存文档和内容
        documentRepository.save(document);
        contentRepository.save(content);

        return document;
    }




    // 根據 userId 查詢所有文檔
    public List<Document> getDocumentsByUserId(Integer userId) {
        return documentRepository.findByUser_UserId(userId);
    }

    // 根據 documentId 查詢單一文檔
    public Document getDocumentById(Integer documentId) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        return documentOptional.orElse(null);  // 如果找不到文檔，返回 null
    }

    // 更新文檔
    public Document updateDocument(Integer documentId, UpdateDocumentDTO updateDocumentDTO) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (documentOptional.isEmpty()) {
            return null;  // 如果文檔不存在，返回 null
        }

        Document document = documentOptional.get();
        document.setTitle(updateDocumentDTO.getTitle());
        document.setUpdatedAt(LocalDateTime.now());

        // 更新文檔內容
        Content content = document.getContent();
        content.setHtmlContent(updateDocumentDTO.getHtmlContent());

        // 儲存更新後的內容
        contentRepository.save(content);

        // 儲存更新後的文檔
        return documentRepository.save(document);
    }

    // 刪除文檔
    public boolean deleteDocument(Integer documentId) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (documentOptional.isEmpty()) {
            return false;  // 如果找不到文檔，返回 false
        }

        documentRepository.deleteById(documentId);
        return true;  // 刪除成功
    }
}
