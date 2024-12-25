package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PomodoroDTO {

    private Integer pomodoroId;
    private String pomodoroName;
    private Integer workDuration;
    private Integer breakDuration;
    private Integer totalTime;
    
    private Integer userId;

    public PomodoroDTO(Integer pomodoroId, String pomodoroName, Integer userId ) {
		this.pomodoroId = pomodoroId;
		this.pomodoroName = pomodoroName;
	}
    
    public PomodoroDTO(Integer pomodoroId, Integer workDuration, Integer breakDuration, Integer totalTime, Integer userId) {
		this.pomodoroId = pomodoroId;
		this.workDuration = workDuration;
		this.breakDuration = breakDuration;
		this.totalTime = totalTime;
		this. userId = userId;
	}
    public PomodoroDTO(Integer pomodoroId, Integer userId) {
		this.pomodoroId = pomodoroId;
		this. userId = userId;
	}
}
