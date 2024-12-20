package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PomodoroDTO {

    private String pomodoroId;
    private String pomodoroName;
    private Integer workDuration;
    private Integer breakDuration;
    private Integer totalTime;
    private Integer userId;

}
