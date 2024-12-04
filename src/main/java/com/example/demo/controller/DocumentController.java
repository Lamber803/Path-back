package com.example.demo.controller;

import com.example.demo.model.dto.CreateDocumentDTO;
import com.example.demo.model.dto.DocumentDTO;
import com.example.demo.model.dto.UpdateDocumentDTO;
import com.example.demo.model.entity.Document;
import com.example.demo.service.DocumentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserService userService;  // 假設您有 UserService 來處理使用者相關邏輯

    @PostMapping("/create")
    public ResponseEntity<DocumentDTO> createDocument(@RequestParam Integer userId, 
                                                      @RequestParam(required = false) String title,
                                                      @RequestParam(required = false) String htmlContent,
                                                      @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        try {
            // 創建 CreateDocumentDTO 對象
            CreateDocumentDTO createDocumentDTO = new CreateDocumentDTO(title, htmlContent);

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
                    document.getContent().getHtmlContent()  // 確保 content 不為 null
                );
            } else {
                documentDTO = new DocumentDTO(
                    document.getDocumentId(),
                    document.getUser().getUserId(),
                    document.getTitle(),
                    document.getCreatedAt(),
                    document.getUpdatedAt(),
                    "No content"  // 預設安全內容，避免 NPE
                );
            }

            // 返回創建的 DocumentDTO
            return new ResponseEntity<>(documentDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            // 打印錯誤堆疊跟蹤
            e.printStackTrace();
            // 如果創建過程中有錯誤，返回 500 錯誤
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 查詢所有文檔（假設是查詢當前用戶的文檔）
    @GetMapping("/search")
    public ResponseEntity<List<DocumentDTO>> getDocuments(@RequestParam Integer userId) {
        try {
            // 呼叫 DocumentService 查詢該用戶的所有文檔
            List<Document> documents = documentService.getDocumentsByUserId(userId);

            // 將所有 Document 實體轉換成 DocumentDTO
            List<DocumentDTO> documentDTOs = documents.stream()
                    .map(document -> new DocumentDTO(
                            document.getDocumentId(),
                            document.getUser().getUserId(),
                            document.getTitle(),
                            document.getCreatedAt(),
                            document.getUpdatedAt(),
                            document.getContent().getHtmlContent(),
                            "http://localhost:5173/documents/search/" + document.getDocumentId() // 构造跳转链接
                            ))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(documentDTOs, HttpStatus.OK);
        } catch (Exception e) {
            // 如果查詢過程中有錯誤，返回 500 錯誤
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

 // 查詢單一文檔
    @GetMapping("/search/{documentId}")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable Integer documentId) {
        try {
            // 呼叫 DocumentService 查詢指定 ID 的文檔
            Document document = documentService.getDocumentById(documentId);

            // 檢查文檔是否存在
            if (document == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 如果找不到文檔，返回 404
            }

            // 將 Document 實體轉換成 DocumentDTO 並返回
            DocumentDTO documentDTO = new DocumentDTO(
                    document.getDocumentId(),
                    document.getUser().getUserId(),
                    document.getTitle(),
                    document.getCreatedAt(),
                    document.getUpdatedAt(),
                    document.getContent().getHtmlContent()
            );

            return new ResponseEntity<>(documentDTO, HttpStatus.OK); // 返回 200 OK 和文檔信息
        } catch (Exception e) {
            // 返回 500 错误
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 更新文檔
    @PutMapping("/update/{documentId}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Integer documentId,
                                                      @RequestBody UpdateDocumentDTO updateDocumentDTO) {
        try {
            // 呼叫 DocumentService 更新文檔
            Document updatedDocument = documentService.updateDocument(documentId, updateDocumentDTO);

            // 檢查文檔是否存在
            if (updatedDocument == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 將更新後的 Document 實體轉換成 DocumentDTO 並返回
            DocumentDTO documentDTO = new DocumentDTO(
                    updatedDocument.getDocumentId(),
                    updatedDocument.getUser().getUserId(),
                    updatedDocument.getTitle(),
                    updatedDocument.getCreatedAt(),
                    updatedDocument.getUpdatedAt(),
                    updatedDocument.getContent().getHtmlContent()
            );

            return new ResponseEntity<>(documentDTO, HttpStatus.OK);
        } catch (Exception e) {
            // 如果更新過程中有錯誤，返回 500 錯誤
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 刪除文檔
    @DeleteMapping("/{documentId}")
    public ResponseEntity<HttpStatus> deleteDocument(@PathVariable Integer documentId) {
        try {
            // 呼叫 DocumentService 刪除文檔
            boolean isDeleted = documentService.deleteDocument(documentId);

            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 刪除成功
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // 沒有找到文檔
            }
        } catch (Exception e) {
            // 如果刪除過程中有錯誤，返回 500 錯誤
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
