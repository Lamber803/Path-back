//package com.example.demo.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.demo.model.entity.Document;
//import com.example.demo.model.entity.Attachment;
//import com.example.demo.service.AttachmentService;
//import com.example.demo.service.DocumentService;
//
//import java.io.IOException;
//import java.util.Optional;
//@RequestMapping("/api/files")
//public class AttachmentController {
//
//    @Autowired
//    private AttachmentService attachmentService;
//
//    @Autowired
//    private DocumentService documentService;
//
//    // 文件上傳接口
//    @PostMapping("/upload/{documentId}")
//    public ResponseEntity<Attachment> uploadFile(
//            @PathVariable Integer documentId, // 使用 PathVariable 獲取路徑中的 documentId
//            @RequestParam MultipartFile file) {  // 使用 RequestParam 獲取上傳的文件
//        try {
//            // 通過 documentId 查找 Document 實體
//            Optional<Document> documentOptional = documentService.findById(documentId);
//            if (documentOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 如果找不到文檔
//            }
//
//            Document document = documentOptional.get();  // 獲取對應的 Document
//
//            // 上傳文件并創建附件
//            Attachment attachment = attachmentService.uploadFile(file, document);
//
//            // 返回成功的響應
//            return ResponseEntity.status(HttpStatus.CREATED).body(attachment);
//        } catch (IOException e) {
//            // 錯誤處理
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    // 文件下载接口
//    @GetMapping("/uploads/{fileName:.+}")
//    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
//        // 調用 AttachmentService 来提供文件資源
//        return attachmentService.serveFile(fileName);
//    }
//
//    
//}