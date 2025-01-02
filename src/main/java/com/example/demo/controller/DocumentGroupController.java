package com.example.demo.controller;

import com.example.demo.exception.DocumentGroupNotFoundException;
import com.example.demo.model.dto.DocumentGroupDTO;
import com.example.demo.model.entity.DocumentGroup;
import com.example.demo.service.DocumentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-groups")
public class DocumentGroupController {

    @Autowired
    private DocumentGroupService documentGroupService;

    // 創建文檔組
    @PostMapping("/create")
    public ResponseEntity<DocumentGroup> createDocumentGroup(@RequestBody DocumentGroupDTO documentGroupDTO) {
        try {
            DocumentGroup createdGroup = documentGroupService.createDocumentGroup(documentGroupDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
        } catch (RuntimeException e) {
            // 捕捉異常
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 獲取指定用戶文檔組
    @GetMapping("/user")
    public ResponseEntity<List<DocumentGroupDTO>> getAllDocumentGroupsByUserId(@RequestParam Integer userId) {
        try {
            // 调用 service 层获取文档组列表
            List<DocumentGroupDTO> documentGroups = documentGroupService.getAllDocumentGroupsByUserId(userId);
            return ResponseEntity.ok(documentGroups);
        } catch (Exception e) {
            // 处理任何异常并返回500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 獲取文檔組相關訊息
    @GetMapping
    public ResponseEntity<DocumentGroupDTO> getDocumentGroupById(@RequestParam Integer groupId) {
        try {
            DocumentGroupDTO documentGroupDTO = documentGroupService.getDocumentGroupById(groupId);
            return ResponseEntity.ok(documentGroupDTO);
        } catch (DocumentGroupNotFoundException e) {
            // 如果文檔組不存在，返回 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            //處理其他異常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 刪除指定文檔組
    @DeleteMapping
    public ResponseEntity<Void> deleteDocumentGroup(@RequestParam Integer groupId) {
        try {
            documentGroupService.deleteDocumentGroup(groupId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 返回204 No Content
        } catch (DocumentGroupNotFoundException e) {
            // 如果文檔組不存在，返回 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            // 處理其他異常，返回 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
