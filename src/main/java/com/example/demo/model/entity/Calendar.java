package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne(fetch = FetchType.LAZY)  // 多對一，
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 關聯User

    @Column(name = "event_title", nullable = false, columnDefinition = "varchar(255)")  // 事件標題
    private String eventTitle;  // 事件標題

    @Column(name = "event_location", columnDefinition = "varchar(255)")  // 事件地點（可選）
    private String eventLocation;  // 事件地點

    @Column(name = "event_start_time")  // 事件開始時間
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventStartTime;  

    @Column(name = "event_end_time")  // 事件節束時間
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventEndTime;  

    @Column(name = "event_mood", columnDefinition = "varchar(20)")  // 事件心情
    private String eventMood;  

    @Column(name = "event_color", columnDefinition = "varchar(20)")  // 事件颜色（使用 HEX 格式）
    private String eventColor;  

    @Column(name = "event_repeat", columnDefinition = "varchar(20)")  // 重複規則
    private String eventRepeat;  

    @Column(name = "is_completed", nullable = false)  // 是否已完成
    private Boolean isCompleted;  // 事件是否完成

}
