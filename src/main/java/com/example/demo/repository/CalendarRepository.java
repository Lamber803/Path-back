package com.example.demo.repository;

import com.example.demo.model.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {

    // 根據用戶ID查詢所有事件
    List<Calendar> findByUser_UserId(Integer userId);
    
    // 根據事件ID和用戶ID查詢單個事件
    Optional<Calendar> findByEventIdAndUser_UserId(Integer eventId, Integer userId);
    
    // 根據用戶ID和完成狀態查詢事件
    List<Calendar> findByUser_UserIdAndIsCompleted(Integer userId, Boolean isCompleted);
}
