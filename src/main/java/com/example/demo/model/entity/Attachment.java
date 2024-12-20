package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主键自动生成，使用数据库的自增特性
    @Column(name = "attachment_id")
    private Integer attachmentId;  // 附件ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)  // 外键，关联到 documents 表
    private Document document;  // 关联到 Document 表（假设有 Document 类，代表文档）

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType;  // 文件类型，使用枚举类型（image 或 file）

    @Column(name = "file_url", nullable = false, length = 255)
    private String fileUrl;  // 文件存储的路径或 URL

    @Column(name = "file_size")
    private Long fileSize;  // 文件大小，单位字节

    @Column(name = "file_name", length = 255)
    private String fileName;  // 文件名

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 创建时间

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;  // 更新时间

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 枚举类型：文件类型（图片或普通文件）
    public enum FileType {
        IMAGE,
        FILE,
        PDF,
        WORD,
        EXCEL,
        TEXT
    }

}
