package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;  // 事件 ID

    @ManyToOne(fetch = FetchType.LAZY)  // 多对一关联，用戶ID
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 关联到 User 实体

    @Column(name = "event_title", nullable = false, columnDefinition = "varchar(255)")  // 事件标题
    private String eventTitle;  // 事件标题

    @Column(name = "event_location", columnDefinition = "varchar(255)")  // 事件地点（可选）
    private String eventLocation;  // 事件地点

    @Column(name = "event_start_time", nullable = false)  // 事件开始时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventStartTime;  // 事件开始时间（日期时间类型）

    @Column(name = "event_end_time", nullable = false)  // 事件结束时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventEndTime;  // 事件结束时间（日期时间类型）

    @Column(name = "event_mood", columnDefinition = "varchar(20)")  // 事件心情（例如：开心、中立、悲伤）
    private String eventMood;  // 事件心情

    @Column(name = "event_color", nullable = false, columnDefinition = "varchar(20)")  // 事件颜色（使用 HEX 格式）
    private String eventColor;  // 事件颜色（如 #FF5733）

    @Column(name = "event_repeat", nullable = false, columnDefinition = "varchar(20)")  // 重复规则
    private String eventRepeat;  // 事件重复规则（例如：none, daily, weekly, monthly）

    @Column(name = "is_completed", nullable = false)  // 是否已完成
    private Boolean isCompleted;  // 事件是否完成

}
