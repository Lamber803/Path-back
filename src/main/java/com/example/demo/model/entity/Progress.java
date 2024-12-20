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
    private Integer progressId;  // 进度记录的唯一标识

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 进度属于哪个用户

    @Column(name = "progress_type", nullable = false)
    private String progressType;  // 进度类型（例如：读书、运动）

    @Column(name = "current_value", nullable = false)
    private Double currentValue;  // 当前进度值

    @Column(name = "target_value", nullable = false)
    private Double targetValue;  // 目标进度值

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;  // 进度开始时间

    @Column(name = "end_date")
    private LocalDateTime endDate;  // 进度结束时间（可选）

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;  // 是否完成

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 创建时间

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;  // 更新时间
}
