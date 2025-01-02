package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主鍵自增
    @Column(name = "attachment_id")
    private Integer attachmentId;  // 附件ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)  // 外鍵關聯Document
    private Document document;  

    @Column(name = "file_url", nullable = false, length = 255)
    private String fileUrl;  // 文件存儲的路徑

    @Column(name = "file_name", length = 255)
    private String fileName;  // 文件名

    @Column(name = "file_size")
    private Long fileSize;  // 文件大小

    // 新增：文件类型字段
    @Enumerated(EnumType.STRING)  // 使用枚舉轉成字符串
    @Column(name = "file_type", nullable = false)
    private FileType fileType;  // 文件類型字段，可能是 IMAGE, FILE, PDF, WORD, EXCEL, TEXT 等

    // 枚舉類型
    public enum FileType {
        IMAGE,
        FILE,
        PDF,
        WORD,
        EXCEL,
        TEXT
    }
}
