package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentGroupDTO {
    private Integer groupId;  // 群組ID
    private Integer userId;  // 群組創建者，與 `User` 一對多關聯
    private String groupName;  // 群組名稱
    private List<DocumentDTO> documents;  // 群組包含的文檔列表
    
}
