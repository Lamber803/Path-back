package com.example.demo.service;

import com.example.demo.model.dto.CalendarDTO;
import com.example.demo.model.entity.Calendar;
import com.example.demo.model.entity.User;
import com.example.demo.repository.CalendarRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.CalendarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private UserRepository userRepository;

    // 保存新的 Calendar 事件
    @Transactional
    public Calendar saveCalendarEvent(CalendarDTO calendarDTO) {
        Optional<User> userOptional = userRepository.findById(calendarDTO.getUserId());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用戶不存在");
        }

        User user = userOptional.get();

        Calendar calendar = new Calendar();
        calendar.setEventTitle(calendarDTO.getEventTitle());
        calendar.setEventLocation(calendarDTO.getEventLocation());
        calendar.setEventStartTime(calendarDTO.getEventStartTime());
        calendar.setEventEndTime(calendarDTO.getEventEndTime());
        calendar.setEventMood(calendarDTO.getEventMood());
        calendar.setEventColor(calendarDTO.getEventColor());
//        calendar.setEventRepeat(calendarDTO.getEventRepeat());
        calendar.setIsCompleted(false);  // 初始為未完成
        calendar.setUser(user);

        return calendarRepository.save(calendar);
    }

    // 根據用戶ID獲取所有事件
    public List<CalendarDTO> getAllEventsForUser(Integer userId) {
        List<Calendar> calendars = calendarRepository.findByUser_UserId(userId);
        return calendars.stream()
                .map(calendar -> new CalendarDTO(
                        calendar.getEventId(),
                        calendar.getUser().getUserId(),
                        calendar.getEventTitle(),
                        calendar.getEventLocation(),
                        calendar.getEventStartTime(),
                        calendar.getEventEndTime(),
                        calendar.getEventMood(),
                        calendar.getEventColor(),
//                        calendar.getEventRepeat(),
                        calendar.getIsCompleted()
                ))
                .collect(Collectors.toList());
    }

    // 更新事件
    public Calendar updateEvent(CalendarDTO calendarDTO) {
        // 查找對應事件
        Calendar calendar = calendarRepository.findByEventIdAndUser_UserId(calendarDTO.getEventId(), calendarDTO.getUserId())
                .orElseThrow(() -> new CalendarNotFoundException("事件未找到"));

        
        if (calendarDTO.getEventTitle() != null) {
            calendar.setEventTitle(calendarDTO.getEventTitle());
        }
        if (calendarDTO.getEventLocation() != null) {
            calendar.setEventLocation(calendarDTO.getEventLocation());
        }
        if (calendarDTO.getEventStartTime() != null) {
            calendar.setEventStartTime(calendarDTO.getEventStartTime());
        }
        if (calendarDTO.getEventEndTime() != null) {
            calendar.setEventEndTime(calendarDTO.getEventEndTime());
        }
        if (calendarDTO.getEventMood() != null) {
            calendar.setEventMood(calendarDTO.getEventMood());
        }
        if (calendarDTO.getEventColor() != null) {
            calendar.setEventColor(calendarDTO.getEventColor());
        }

        // 保存并返回更新後的事件
        return calendarRepository.save(calendar);
    }


    public Calendar toggleEventCompletion(Integer eventId, Integer userId, boolean isCompleted) {
        // 查找指定事件
        Calendar calendar = calendarRepository.findByEventIdAndUser_UserId(eventId, userId)
                .orElseThrow(() -> new CalendarNotFoundException("事件未找到"));

        // 根據isCompleted值更新事件状态
        calendar.setIsCompleted(isCompleted);
        return calendarRepository.save(calendar);  // 保存更新后的事件
    }


    // 刪除事件
    public void deleteEvent(CalendarDTO calendarDTO) {
        Calendar calendar = calendarRepository.findByEventIdAndUser_UserId(calendarDTO.getEventId(), calendarDTO.getUserId())
                .orElseThrow(() -> new CalendarNotFoundException("事件未找到"));

        calendarRepository.delete(calendar);
    }
}
