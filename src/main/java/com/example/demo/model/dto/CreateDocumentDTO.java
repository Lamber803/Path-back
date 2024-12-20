package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDocumentDTO {
    private String title;  // 文檔標題
    private String htmlContent;  // 用戶通過 Quill 編輯器輸入的 HTML 內容
}
