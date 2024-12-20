package com.example.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer documentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 這個文檔的創建者

    @Column(name = "title", nullable = false, columnDefinition = "varchar(255)")
    private String title;  // 文檔標題

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 創建時間

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;  // 更新時間

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Content content;  // 該文檔的內容
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<Attachment> files;  // 存储与文档关联的文件
}
