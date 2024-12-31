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

    // 创建新的文档组
    @PostMapping("/create")
    public ResponseEntity<DocumentGroup> createDocumentGroup(@RequestBody DocumentGroupDTO documentGroupDTO) {
        try {
            // 调用 service 层创建文档组
            DocumentGroup createdGroup = documentGroupService.createDocumentGroup(documentGroupDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
        } catch (RuntimeException e) {
            // 捕获异常并返回400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 获取指定用户的所有文档组
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

    // 获取指定文档组的详细信息
    @GetMapping
    public ResponseEntity<DocumentGroupDTO> getDocumentGroupById(@RequestParam Integer groupId) {
        try {
            // 调用 service 层根据ID获取文档组
            DocumentGroupDTO documentGroupDTO = documentGroupService.getDocumentGroupById(groupId);
            return ResponseEntity.ok(documentGroupDTO);
        } catch (DocumentGroupNotFoundException e) {
            // 如果文档组不存在，返回 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            // 处理其他异常，返回 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 删除指定文档组及其所有文档
    @DeleteMapping
    public ResponseEntity<Void> deleteDocumentGroup(@RequestParam Integer groupId) {
        try {
            // 调用 service 层删除文档组
            documentGroupService.deleteDocumentGroup(groupId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 返回204 No Content
        } catch (DocumentGroupNotFoundException e) {
            // 如果文档组不存在，返回 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            // 处理其他异常，返回 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
