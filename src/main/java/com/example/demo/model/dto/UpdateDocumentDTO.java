package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDocumentDTO {
    private Integer documentId;
    private String title;  // 文檔標題
    private String htmlContent;  // 更新後的 HTML 內容
}
