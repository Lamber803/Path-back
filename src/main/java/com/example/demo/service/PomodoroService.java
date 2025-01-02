package com.example.demo.service;

import com.example.demo.exception.PomodoroNotFoundException;
import com.example.demo.model.dto.PomodoroDTO;
import com.example.demo.model.entity.Pomodoro;
import com.example.demo.repository.PomodoroRepository;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PomodoroService {

    @Autowired
    private PomodoroRepository pomodoroRepository;

    @Autowired
    private UserRepository userRepository; // 用于查找用户

    // 保存新的Pomodoro紀錄
    @Transactional
    public Pomodoro savePomodoro(PomodoroDTO pomodoroDTO) {
        Optional<User> userOptional = userRepository.findById(pomodoroDTO.getUserId());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        User user = userOptional.get();

        Pomodoro pomodoro = new Pomodoro();
        pomodoro.setPomodoroName(pomodoroDTO.getPomodoroName());
        pomodoro.setWorkDuration(pomodoroDTO.getWorkDuration());
        pomodoro.setBreakDuration(pomodoroDTO.getBreakDuration());
        pomodoro.setTotalTime(0);  // 初始累计时间
        pomodoro.setUser(user);

        return pomodoroRepository.save(pomodoro);
    }

    	// 獲取某用户所有的Pomodoro紀錄
    public List<PomodoroDTO> getAllPomodorosForUser(Integer userId) {
        // 從數據庫獲取該用戶的所有Pomodoro紀錄
        List<Pomodoro> pomodoros = pomodoroRepository.findByUser_UserId(userId);

        // 將所有Pomodoro實體轉換成PomodoroDTO
        List<PomodoroDTO> pomodoroDTOs = pomodoros.stream()
                .map(pomodoro -> new PomodoroDTO(
                        pomodoro.getPomodoroId(),
                        pomodoro.getPomodoroName(),
                        pomodoro.getWorkDuration(),
                        pomodoro.getBreakDuration(),
                        pomodoro.getTotalTime(),
                        pomodoro.getUser().getUserId()
                ))
                .collect(Collectors.toList());

        // 返回PomodoroDTO列表
        return pomodoroDTOs;
    }


    public Pomodoro updatePomodoroName(PomodoroDTO pomodoroDTO) {
        // 從數據庫查找 Pomodoro 實體
        Pomodoro pomodoro = pomodoroRepository.findByPomodoroIdAndUser_UserId(pomodoroDTO.getPomodoroId(), pomodoroDTO.getUserId())
                .orElseThrow(() -> new PomodoroNotFoundException("Pomodoro not found"));

        // 更新名稱
        pomodoro.setPomodoroName(pomodoroDTO.getPomodoroName());

        // 保存更新後的 Pomodoro 對象
        return pomodoroRepository.save(pomodoro);
    }

    public Pomodoro updatePomodoroTimer(PomodoroDTO pomodoroDTO) {
        // 從數據庫查找 Pomodoro 實體
        Pomodoro pomodoro = pomodoroRepository.findByPomodoroIdAndUser_UserId(pomodoroDTO.getPomodoroId(), pomodoroDTO.getUserId())
                .orElseThrow(() -> new PomodoroNotFoundException("Pomodoro not found"));

        // 更新計時器設置
        pomodoro.setWorkDuration(pomodoroDTO.getWorkDuration());
        pomodoro.setBreakDuration(pomodoroDTO.getBreakDuration());
        pomodoro.setTotalTime(pomodoro.getTotalTime()+ pomodoroDTO.getTotalTime());

        // 保存更新後的 Pomodoro 對象
        return pomodoroRepository.save(pomodoro);
    }



    // 刪除Pomodoro紀錄
    public void deletePomodoro(PomodoroDTO pomodoroDTO) {
        // 使用 pomodoroId 和 userId 查找對應的 Pomodoro 紀錄，確保它屬於當前用戶
        Pomodoro pomodoro = pomodoroRepository.findByPomodoroIdAndUser_UserId(pomodoroDTO.getPomodoroId(), pomodoroDTO.getUserId())
                .orElseThrow(() -> new PomodoroNotFoundException("Pomodoro not found or does not belong to user"));

        // 刪除 Pomodoro 實體
        pomodoroRepository.delete(pomodoro);
    }

}
