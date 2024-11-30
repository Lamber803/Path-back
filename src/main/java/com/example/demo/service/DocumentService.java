package com.example.demo.service;

import com.example.demo.model.dto.CreateDocumentDTO;
import com.example.demo.model.dto.UpdateDocumentDTO;
import com.example.demo.model.entity.Document;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.Content;
import com.example.demo.repository.DocumentRepository;
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
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;  // 用於查找用戶

    // 創建新文檔
    public Document createDocument(Integer userId, CreateDocumentDTO createDocumentDTO, List<MultipartFile> files) {
        // 查找用戶
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("找不到 ID 為 " + userId + " 的使用者");
        }

        User user = userOptional.get();  // 獲取 User 物件

        // 創建 Document 實體
        Document document = new Document();
        document.setUser(user);  // 設置 User 實體
        document.setTitle(createDocumentDTO.getTitle());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        

        // 處理文件上傳
        String htmlContent = createDocumentDTO.getHtmlContent();

        // 如果有文件上傳，處理圖片或檔案 URL
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    // 上傳檔案並獲得 URL
                    String fileUrl = fileService.uploadFile(file);

                    // 替換 HTML 中的圖片或檔案 URL
                    // 假設 HTML 中的文件 URL 使用 `file://` 格式
                    htmlContent = htmlContent.replace("file://", fileUrl);
                } catch (IOException e) {
                    throw new RuntimeException("文件上傳失敗", e);
                }
            }
        }

        // 創建並儲存文檔內容
        Content content = new Content();
        content.setHtmlContent(htmlContent);
        content.setDocument(document);  // 將內容與文檔關聯
        document.setContent(content);    // 設置 Document 的 Content
        
     // 儲存文檔
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
