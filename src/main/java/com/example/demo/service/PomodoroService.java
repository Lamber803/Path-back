package com.example.demo.service;

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

@Service
public class PomodoroService {

    @Autowired
    private PomodoroRepository pomodoroRepository;

    @Autowired
    private UserRepository userRepository; // 用于查找用户

    // 保存新的Pomodoro记录
    @Transactional
    public Pomodoro savePomodoro(PomodoroDTO pomodoroDTO) {
        Optional<User> userOptional = userRepository.findById(pomodoroDTO.getUserId());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        User user = userOptional.get();

        Pomodoro pomodoro = new Pomodoro();
        pomodoro.setPomodoroId(pomodoroDTO.getPomodoroId());
        pomodoro.setPomodoroName(pomodoroDTO.getPomodoroName());
        pomodoro.setWorkDuration(pomodoroDTO.getWorkDuration());
        pomodoro.setBreakDuration(pomodoroDTO.getBreakDuration());
        pomodoro.setTotalTime(0);  // 初始累计时间
        pomodoro.setUser(user);

        return pomodoroRepository.save(pomodoro);
    }

    // 获取某用户所有的Pomodoro记录
    public List<Pomodoro> getAllPomodorosForUser(Integer userId) {
        return pomodoroRepository.findByUser_UserId(userId);
    }

    // 更新Pomodoro记录
    public Pomodoro updatePomodoro(Integer pomodoroId, PomodoroDTO pomodoroDTO) {
        Pomodoro existingPomodoro = pomodoroRepository.findById(pomodoroId)
                .orElseThrow(() -> new RuntimeException("Pomodoro记录不存在"));

        existingPomodoro.setPomodoroName(pomodoroDTO.getPomodoroName());
        existingPomodoro.setWorkDuration(pomodoroDTO.getWorkDuration());
        existingPomodoro.setBreakDuration(pomodoroDTO.getBreakDuration());
        existingPomodoro.setTotalTime(pomodoroDTO.getTotalTime()); // 更新累计时间

        return pomodoroRepository.save(existingPomodoro);
    }

    // 删除Pomodoro记录
    public void deletePomodoro(Integer pomodoroId) {
        pomodoroRepository.deleteById(pomodoroId);
    }
}
