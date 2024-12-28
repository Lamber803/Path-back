package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDTO {
    
    private Integer eventId;  // 事件ID
    private Integer userId;   // 用戶ID
    private String eventTitle; // 事件標題
    private String eventLocation; // 事件地點
    private Date eventStartTime; // 事件開始時間
    private Date eventEndTime; // 事件結束時間
    private String eventMood; // 事件心情
    private String eventColor; // 事件顏色
//    private String eventRepeat; // 重復規則
    private Boolean isCompleted; // 是否完成
    	
    public CalendarDTO(Integer eventId, Integer userId,Boolean isCompleted) {
		this.eventId = eventId;
		this.userId = userId;
		this.isCompleted = isCompleted;
	}
}
