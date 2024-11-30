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
}
