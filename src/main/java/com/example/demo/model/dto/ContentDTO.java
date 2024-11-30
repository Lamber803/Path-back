package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {
    private Integer contentId;
    private Integer documentId;  // 內容所屬的文檔 ID
    private String htmlContent;  // 存儲的 HTML 格式內容
}
