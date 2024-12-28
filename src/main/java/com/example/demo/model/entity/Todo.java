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
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Integer todoId;  // 待辦事項 ID

    @ManyToOne(fetch = FetchType.LAZY)  // 多對一關聯使用者
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 關聯到 User 實體

    @Column(name = "todo_text", nullable = false, columnDefinition = "varchar(255)")  // 待辦事項內容
    private String todoText;  // 待辦事項文本

    @Column(name = "is_completed", nullable = false)  // 是否已完成
    private Boolean isCompleted;  // 任務是否已完成
}
