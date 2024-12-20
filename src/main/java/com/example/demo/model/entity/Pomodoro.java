package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pomodoro")
public class Pomodoro {

    @Id
    @Column(name = "pomodoro_id")
    private Integer pomodoroId;  // Pomodoro ID

    @ManyToOne(fetch = FetchType.LAZY)  // 多對一關聯，用戶ID
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 關聯到 User 實體

    @Column(name = "pomodoro_name", nullable = false, columnDefinition = "varchar(255)")  // Pomodoro 名稱
    private String pomodoroName;

    @Column(name = "work_duration", nullable = false)  // 工作時間（秒）
    private Integer workDuration;  // 工作時間（以秒為單位）

    @Column(name = "break_duration", nullable = false)  // 休息時間（秒）
    private Integer breakDuration;  // 休息時間（以秒為單位）

    @Column(name = "total_time", nullable = false)  // 累計時間（秒）
    private Integer totalTime;  // 累計時間（以秒為單位）

}
