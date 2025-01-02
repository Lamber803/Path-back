package com.example.demo.controller;

import com.example.demo.model.dto.CreateDocumentDTO;
import com.example.demo.model.dto.DocumentDTO;
import com.example.demo.model.dto.UpdateDocumentDTO;
import com.example.demo.model.entity.Attachment;
import com.example.demo.model.entity.Document;
import com.example.demo.service.AttachmentService;
import com.example.demo.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AttachmentService attachmentService;

   
    // 創建文檔
    @PostMapping("/create")
    public ResponseEntity<DocumentDTO> createDocument(@RequestParam Integer userId, 
                                                      @RequestParam(required = false) String title,
                                                      @RequestParam(required = false) String htmlContent,
                                                      @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                                      @RequestParam Integer groupId) {  // 接收 groupId
        try {
            // 創建 CreateDocumentDTO 對象
            CreateDocumentDTO createDocumentDTO = new CreateDocumentDTO(title, htmlContent, groupId);

            // 呼叫 DocumentService 來創建文檔
            Document document = documentService.createDocument(userId, createDocumentDTO, files);

            // 檢查 document 的 content 是否為 null，避免 NullPointerException
            DocumentDTO documentDTO;
            if (document.getContent() != null) {
                documentDTO = new DocumentDTO(
                    document.getDocumentId(),
                    document.getUser().getUserId(),
                    document.getTitle(),
                    document.getCreatedAt(),
                    document.getUpdatedAt(),
                    document.getContent().getHtmlContent()
                );
            } else {
                documentDTO = new DocumentDTO(
                    document.getDocumentId(),
                    document.getUser().getUserId(),
                    document.getTitle(),
                    document.getCreatedAt(),
                    document.getUpdatedAt(),
                    "No content"
                );
            }

            // 返回創建的 DocumentDTO
            return new ResponseEntity<>(documentDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            // 打印錯誤堆疊跟蹤
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // 獲取群組內所有文檔
    @GetMapping("/group")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByGroupId(@RequestParam Integer groupId) {
        try {
            List<DocumentDTO> documentDTOs = documentService.getDocumentsByGroupId(groupId);
            return ResponseEntity.ok(documentDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 獲取指定文檔內訊息
    @GetMapping
    public ResponseEntity<DocumentDTO> getDocumentById(@RequestParam Integer documentId) {
        try {
            DocumentDTO documentDTO = documentService.getDocumentById(documentId);
            return ResponseEntity.ok(documentDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 更新文檔
    @PutMapping("/update")
    public ResponseEntity<DocumentDTO> updateDocument(
    		@RequestParam Integer documentId,
            @RequestBody UpdateDocumentDTO updateDocumentDTO) {

        Document updatedDocument = documentService.updateDocument(documentId, updateDocumentDTO);
        if (updatedDocument != null) {
            DocumentDTO documentDTO = new DocumentDTO(
                    updatedDocument.getDocumentId(),
                    updatedDocument.getUser().getUserId(),
                    updatedDocument.getTitle(),
                    updatedDocument.getCreatedAt(),
                    updatedDocument.getUpdatedAt(),
                    updatedDocument.getContent().getHtmlContent(),
                    updatedDocument.getDocumentGroup().getGroupId()
            );
            return ResponseEntity.ok(documentDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 刪除文檔
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteDocument(@RequestParam Integer documentId) {
        boolean isDeleted = documentService.deleteDocument(documentId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
