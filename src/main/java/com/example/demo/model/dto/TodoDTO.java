package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.example.demo.model.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    
	private Integer todoId;  // 待辦事項 ID

	private Integer userId; // 使用者ID

    private String todoText;  // 待辦事項文本

    private Boolean isCompleted;  // 任務是否已完成
    
    public TodoDTO(Integer todoId, String todoText ) {
		this.todoId = todoId;
		this.todoText = todoText;
	}
    public TodoDTO(Integer todoId, Boolean isCompleted ) {
		this.todoId = todoId;
		this.isCompleted = isCompleted;
	}
}