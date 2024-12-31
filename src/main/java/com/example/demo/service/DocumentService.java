package com.example.demo.service;

import com.example.demo.model.dto.CreateDocumentDTO;
import com.example.demo.model.dto.DocumentDTO;
import com.example.demo.model.dto.UpdateDocumentDTO;
import com.example.demo.model.entity.Document;
import com.example.demo.model.entity.DocumentGroup;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.Attachment;
import com.example.demo.model.entity.Content;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.DocumentGroupRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.repository.ContentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private DocumentGroupRepository documentGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private ContentRepository contentRepository;

    // 判斷附件類型
    private Attachment.FileType determineFileType(MultipartFile file) {
        // 獲取文件的 MIME 類型
        String contentType = file.getContentType();

        // 如果是圖片文件，返回 IMAGE 類型
        if (contentType != null && contentType.startsWith("image")) {
            return Attachment.FileType.IMAGE;
        }

        // 否則，返回 FILE 類型
        return Attachment.FileType.FILE;
    }

    // 創建新文檔
    public Document createDocument(Integer userId, CreateDocumentDTO createDocumentDTO, List<MultipartFile> files) {
        // 查找用戶
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("找不到 ID 為 " + userId + " 的用戶");
        }

        User user = userOptional.get();  // 獲取 User 物件
        
        Optional<DocumentGroup> documentGroupOptional = documentGroupRepository.findById(createDocumentDTO.getGroupId());
        if (!documentGroupOptional.isPresent()) {
            throw new RuntimeException("文檔群組不存在");
        }

        DocumentGroup documentGroup = documentGroupOptional.get();  // 獲取 DocumentGroup 物件

        // 創建 Document 實體
        Document document = new Document();
        document.setUser(user);  // 設置 User 物件
        document.setTitle(createDocumentDTO.getTitle());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        document.setDocumentGroup(documentGroup);

        // 獲取 HTML 內容
        String htmlContent = createDocumentDTO.getHtmlContent();

        // 如果有文件上傳，處理圖片或文件的 URL
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    // 上傳文件並獲取 URL
                    String fileUrl = attachmentService.uploadFile(file);  // 上傳文件服務，返回可訪問的 URL（HTTP/HTTPS）

                    // 處理 Blob URL 替換
                    htmlContent = htmlContent.replace("blob://", fileUrl);

                    // 創建附件並保存
                    Attachment attachment = new Attachment();
                    attachment.setDocument(document);
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(fileUrl);
                    attachment.setFileSize(file.getSize());
                    attachment.setFileType(determineFileType(file));  // 根據 MIME 類型設置文件類型

                    // 保存附件信息到數據庫
                    attachmentRepository.save(attachment);

                } catch (IOException e) {
                    throw new RuntimeException("文件上傳失敗", e);
                }
            }
        }

        // 創建並保存文檔內容
        Content content = new Content();
        content.setHtmlContent(htmlContent);
        content.setDocument(document);  // 將內容與文檔關聯
        document.setContent(content);    // 設置 Document 的 Content

        // 保存文檔和內容
        documentRepository.save(document);
        contentRepository.save(content);

        return document;
    }

    // 根據文檔組查詢文檔
//    public List<DocumentDTO> getDocumentsByGroupId(Integer groupId) {
//        List<Document> documents = documentRepository.findByDocumentGroup_GroupId(groupId);
//        return documents.stream()
//                .map(document -> new DocumentDTO(
//                        document.getDocumentId(),
//                        document.getUser().getUserId(),
//                        document.getTitle(),
//                        document.getCreatedAt(),
//                        document.getUpdatedAt(),
//                        document.getContent().getHtmlContent(),
//                        document.getDocumentGroup().getGroupId()
//                ))
//                .collect(Collectors.toList());
//    }
 // 根据文档组查询文档（只返回 DocumentId 和 title）
    public List<DocumentDTO> getDocumentsByGroupId(Integer groupId) {
        List<Document> documents = documentRepository.findByDocumentGroup_GroupId(groupId);
        return documents.stream()
                .map(document -> new DocumentDTO(
                        document.getDocumentId(),
                        document.getTitle()  // 只返回 DocumentId 和 title
                ))
                .collect(Collectors.toList());
    }


    // 根據 userId 查詢所有文檔（目前用不到）
    public List<DocumentDTO> getDocumentsByUserId(Integer userId) {
        List<Document> documents = documentRepository.findByUser_UserId(userId);
        return documents.stream()
                .map(document -> new DocumentDTO(
                        document.getDocumentId(),
                        document.getUser().getUserId(),
                        document.getTitle(),
                        document.getCreatedAt(),
                        document.getUpdatedAt(),
                        document.getContent().getHtmlContent(),
                        document.getDocumentGroup().getGroupId()
                ))
                .collect(Collectors.toList());
    }

    // 根據 documentId 查詢單一文檔
    public DocumentDTO getDocumentById(Integer documentId) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (!documentOptional.isPresent()) {
            throw new RuntimeException("文檔不存在");
        }

        Document document = documentOptional.get();
        return new DocumentDTO(
                document.getDocumentId(),
                document.getUser().getUserId(),
                document.getTitle(),
                document.getCreatedAt(),
                document.getUpdatedAt(),
                document.getContent().getHtmlContent(),
                document.getDocumentGroup().getGroupId()
        );
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

        // 保存更新後的內容
        contentRepository.save(content);

        // 保存更新後的文檔
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
