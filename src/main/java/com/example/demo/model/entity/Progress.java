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
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;  

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @Column(name = "progress_type", nullable = false)
    private String progressType;  // 進度類型

    @Column(name = "current_value", nullable = false)
    private Double currentValue;  // 進度值

    @Column(name = "target_value", nullable = false)
    private Double targetValue;  // 目標值

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;  // 進度開始時間

    @Column(name = "end_date")
    private LocalDateTime endDate;  // 進度結束時間

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;  // 是否完成

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 創建時間

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;  // 更新時間
}
