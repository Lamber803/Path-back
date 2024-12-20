package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private Integer documentId;
    private Integer userId;  // 可傳回使用者 ID，或者使用者詳細資料的簡要信息
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String htmlContent;  // 若不想在 DocumentDTO 裡包含內容，也可以傳回 ContentDTO。
    private String url; // The URL you want to pass
    
    public DocumentDTO(Integer documentId, Integer userId, String title,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            String htmlContent) {
this.documentId = documentId;
this.userId = userId;
this.title = title;
this.createdAt = createdAt;
this.updatedAt = updatedAt;
this.htmlContent = htmlContent;
}
}
