package com.example.demo.repository;

import com.example.demo.model.entity.Pomodoro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PomodoroRepository extends JpaRepository<Pomodoro, Integer> {
    List<Pomodoro> findByUser_UserId(Integer userId);  // 查詢特定使用者的所有文檔
    Optional<Pomodoro> findByPomodoroIdAndUser_UserId(Integer pomodoroId, Integer userId);  // 查詢指定使用者的特定文檔
}
