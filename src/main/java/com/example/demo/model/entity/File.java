package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;  // 該檔案屬於哪個文檔

    @Column(name = "file_name", nullable = false, columnDefinition = "varchar(255)")
    private String fileName;  // 檔案名稱

    @Column(name = "file_path", nullable = false, columnDefinition = "varchar(255)")
    private String filePath;  // 檔案儲存路徑

    @Column(name = "file_type", nullable = false, columnDefinition = "varchar(50)")
    private String fileType;  // 檔案類型（例如圖片、PDF等）

    @Column(name = "file_size", nullable = false)
    private Long fileSize;  // 檔案大小
}
