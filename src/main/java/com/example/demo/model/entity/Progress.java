package com.example.demo.model.entity;

import jakarta.persistence.Column;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
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
public class Progress {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer ProgressId;  // 進度ID

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;  // 所屬用戶

    //@ManyToOne
    //@JoinColumn(name = "file_id", referencedColumnName = "file_id", nullable = false)
    //private File file;  // 關聯的檔案

    @Column(name = "progress_value", nullable = false)
    private Integer progressValue;  // 進度數值（例如：0~100）

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 進度創建時間

}
