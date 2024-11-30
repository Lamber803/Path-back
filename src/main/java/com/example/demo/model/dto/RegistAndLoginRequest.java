package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistAndLoginRequest {
    private String username;
    private String password;
    private String email;
    private String role; // 角色權限
}
