package com.example.demo.controller;

import com.example.demo.model.dto.PomodoroDTO;
import com.example.demo.model.entity.Pomodoro;
import com.example.demo.service.PomodoroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pomodoro")
public class PomodoroController {

    @Autowired
    private PomodoroService pomodoroService;

    // 获取用户的所有Pomodoro记录
    @GetMapping("/tasks/{userId}")
    public ResponseEntity<List<Pomodoro>> getPomodorosForUser(@PathVariable Integer userId) {
        List<Pomodoro> pomodoros = pomodoroService.getAllPomodorosForUser(userId);
        return ResponseEntity.ok(pomodoros);
    }

    // 保存新的Pomodoro记录
    @PostMapping("/tasks")
    public ResponseEntity<Pomodoro> createPomodoro(@RequestBody PomodoroDTO pomodoroDTO) {
        Pomodoro pomodoro = pomodoroService.savePomodoro(pomodoroDTO);
        return ResponseEntity.ok(pomodoro);
    }

    // 更新Pomodoro记录
    @PutMapping("/tasks/{pomodoroId}")
    public ResponseEntity<Pomodoro> updatePomodoro(
            @PathVariable Integer pomodoroId, @RequestBody PomodoroDTO pomodoroDTO) {
        Pomodoro pomodoro = pomodoroService.updatePomodoro(pomodoroId, pomodoroDTO);
        return ResponseEntity.ok(pomodoro);
    }

    // 删除Pomodoro记录
    @DeleteMapping("/tasks/{pomodoroId}")
    public ResponseEntity<Void> deletePomodoro(@PathVariable Integer pomodoroId) {
        pomodoroService.deletePomodoro(pomodoroId);
        return ResponseEntity.noContent().build();
    }
}
