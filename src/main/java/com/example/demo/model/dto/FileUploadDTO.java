package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDTO {
    private String fileName;  // 檔案名稱
    private String fileType;  // 檔案類型
    private Long fileSize;    // 檔案大小
}
