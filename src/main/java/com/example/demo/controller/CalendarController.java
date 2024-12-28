package com.example.demo.controller;

import com.example.demo.model.dto.CalendarDTO;
import com.example.demo.model.entity.Calendar;
import com.example.demo.service.CalendarService;
import com.example.demo.exception.CalendarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    // 獲取用戶的所有事件
    @GetMapping("/events")
    public ResponseEntity<List<CalendarDTO>> getEventsForUser(@RequestParam Integer userId) {
        List<CalendarDTO> events = calendarService.getAllEventsForUser(userId);
        return ResponseEntity.ok(events);
    }

    // 保存新的事件
    @PostMapping("/events")
    public ResponseEntity<Calendar> createEvent(@RequestBody CalendarDTO calendarDTO) {
        Calendar calendar = calendarService.saveCalendarEvent(calendarDTO);
        return ResponseEntity.ok(calendar);
    }

    // 更新事件的標題
    @PutMapping("/events")
    public ResponseEntity<Calendar> updateEvent(@RequestBody CalendarDTO calendarDTO) {
        Calendar calendar = calendarService.updateEvent(calendarDTO);
        return ResponseEntity.ok(calendar);
    }

    @PutMapping("/events/complete")
    public ResponseEntity<?> toggleEventCompletion(
    		@RequestBody CalendarDTO calendarDTO) {  // 用isCompleted来判断是否是标记为完成，还是取消完成
    	try {
            Calendar calendar = calendarService.toggleEventCompletion(calendarDTO.getEventId(), calendarDTO.getUserId(), calendarDTO.getIsCompleted());
            if (calendar != null) {
                return ResponseEntity.ok(calendar);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update event: " + e.getMessage());
        }
    }



    // 刪除事件
    @DeleteMapping("/events")
    public ResponseEntity<Void> deleteEvent(@RequestBody CalendarDTO calendarDTO) {
        try {
            calendarService.deleteEvent(calendarDTO);
            return ResponseEntity.noContent().build(); // 返回204 No Content表示刪除成功
        } catch (CalendarNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 事件未找到
        }
    }
}
