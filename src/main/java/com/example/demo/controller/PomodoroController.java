package com.example.demo.controller;

import com.example.demo.exception.PomodoroNotFoundException;
import com.example.demo.model.dto.PomodoroDTO;
import com.example.demo.model.entity.Pomodoro;
import com.example.demo.service.PomodoroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pomodoro")
public class PomodoroController {

    @Autowired
    private PomodoroService pomodoroService;

    // 获取用户的所有Pomodoro记录
    @GetMapping("/tasks")
    public ResponseEntity<List<PomodoroDTO>> getPomodorosForUser(@RequestParam Integer userId) {
        List<PomodoroDTO> pomodoros = pomodoroService.getAllPomodorosForUser(userId);
        return ResponseEntity.ok(pomodoros);
    }

    // 保存新的Pomodoro记录
    @PostMapping("/tasks")
    public ResponseEntity<Pomodoro> createPomodoro(@RequestBody PomodoroDTO pomodoroDTO) {
        Pomodoro pomodoro = pomodoroService.savePomodoro(pomodoroDTO);
        return ResponseEntity.ok(pomodoro);
    }

    @PutMapping("/tasks/name")
    public ResponseEntity<Pomodoro> updatePomodoroName(@RequestBody PomodoroDTO pomodoroDTO) {
        Pomodoro pomodoro = pomodoroService.updatePomodoroName(pomodoroDTO);
        return ResponseEntity.ok(pomodoro);
    }


    @PutMapping("/tasks/timer")
    public ResponseEntity<Pomodoro> updatePomodoroTimer(@RequestBody PomodoroDTO pomodoroDTO) {
        Pomodoro pomodoro = pomodoroService.updatePomodoroTimer(pomodoroDTO);
        return ResponseEntity.ok(pomodoro);
    }


    @DeleteMapping("/tasks/delete")
    public ResponseEntity<Void> deletePomodoro(@RequestBody PomodoroDTO pomodoroDTO) {
        try {
            // 调用服务层删除记录
            pomodoroService.deletePomodoro(pomodoroDTO);
            return ResponseEntity.noContent().build(); // 返回204 No Content表示删除成功
        } catch (PomodoroNotFoundException ex) {
            // 返回 404 Not Found 状态码，表示 Pomodoro 记录未找到
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            // 捕获其他异常并返回 500 错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
