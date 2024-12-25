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

    // 保存新的Pomodoro记录
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

 // 获取某用户所有的Pomodoro记录
    public List<PomodoroDTO> getAllPomodorosForUser(Integer userId) {
        // 从数据库获取该用户的所有Pomodoro记录
        List<Pomodoro> pomodoros = pomodoroRepository.findByUser_UserId(userId);

        // 将所有Pomodoro实体转换成PomodoroDTO
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
        // 从数据库查找 Pomodoro 实体
        Pomodoro pomodoro = pomodoroRepository.findByPomodoroIdAndUser_UserId(pomodoroDTO.getPomodoroId(), pomodoroDTO.getUserId())
                .orElseThrow(() -> new PomodoroNotFoundException("Pomodoro not found"));

        // 更新名称
        pomodoro.setPomodoroName(pomodoroDTO.getPomodoroName());

        // 保存更新后的 Pomodoro 对象
        return pomodoroRepository.save(pomodoro);
    }

    public Pomodoro updatePomodoroTimer(PomodoroDTO pomodoroDTO) {
        // 从数据库查找 Pomodoro 实体
        Pomodoro pomodoro = pomodoroRepository.findByPomodoroIdAndUser_UserId(pomodoroDTO.getPomodoroId(), pomodoroDTO.getUserId())
                .orElseThrow(() -> new PomodoroNotFoundException("Pomodoro not found"));

        // 更新计时器设置
        pomodoro.setWorkDuration(pomodoroDTO.getWorkDuration());
        pomodoro.setBreakDuration(pomodoroDTO.getBreakDuration());
        pomodoro.setTotalTime(pomodoro.getTotalTime()+ pomodoroDTO.getTotalTime());

        // 保存更新后的 Pomodoro 对象
        return pomodoroRepository.save(pomodoro);
    }



    // 删除Pomodoro记录
    public void deletePomodoro(PomodoroDTO pomodoroDTO) {
        // 使用 pomodoroId 和 userId 查找对应的 Pomodoro 记录，确保它属于当前用户
        Pomodoro pomodoro = pomodoroRepository.findByPomodoroIdAndUser_UserId(pomodoroDTO.getPomodoroId(), pomodoroDTO.getUserId())
                .orElseThrow(() -> new PomodoroNotFoundException("Pomodoro not found or does not belong to user"));

        // 删除 Pomodoro 实体
        pomodoroRepository.delete(pomodoro);
    }

}
