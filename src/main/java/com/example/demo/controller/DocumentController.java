package com.example.demo.controller;

import com.example.demo.model.dto.CreateDocumentDTO;
import com.example.demo.model.dto.DocumentDTO;
import com.example.demo.model.dto.UpdateDocumentDTO;
import com.example.demo.model.entity.Document;
import com.example.demo.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // 創建新文檔
    @PostMapping("/create")
    public ResponseEntity<DocumentDTO> createDocument(
            @RequestParam Integer userId, // 通过 query param 获取 userId
            @RequestParam String createDocumentDTO, // 获取 createDocumentDTO 的 JSON 字符串
            @RequestParam(required = false) List<MultipartFile> files) { // 获取文件

        try {
            // 将 JSON 字符串转换为 CreateDocumentDTO 对象
            ObjectMapper objectMapper = new ObjectMapper();
            CreateDocumentDTO documentDTO = objectMapper.readValue(createDocumentDTO, CreateDocumentDTO.class);

            // 调用 service 层创建文档
            Document document = documentService.createDocument(userId, documentDTO, files);

            // 将文档转换为 DocumentDTO 以返回给前端
            DocumentDTO result = new DocumentDTO(
                    document.getDocumentId(),
                    document.getUser().getUserId(),
                    document.getTitle(),
                    document.getCreatedAt(),
                    document.getUpdatedAt(),
                    document.getContent().getHtmlContent(),
                    document.getDocumentGroup().getGroupId()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            // 错误处理
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 获取指定文档组的所有文档
    @GetMapping("/group")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByGroupId(@RequestParam Integer groupId) {
        try {
            List<DocumentDTO> documentDTOs = documentService.getDocumentsByGroupId(groupId);
            return ResponseEntity.ok(documentDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 获取指定文档的详细信息
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
