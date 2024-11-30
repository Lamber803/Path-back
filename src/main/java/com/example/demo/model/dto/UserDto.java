package com.example.demo.model.dto;

import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.PropertySource;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PropertySource("classpath:messages.properties")
public class UserDto {
	
	@NotNull(message = "{userDto.userId.notNull}")
	@Range(min = 1, max = 10, message = "{userDto.userId.range}")
	private Integer userId; // 使用者ID
	
	@NotNull(message = "{userDto.userName.notNull}")
	@Size(min = 1, max = 10, message = "{userDto.userName.size}")
	private String username; // 使用者名稱
	
	@NotNull(message = "{userDto.email.notNull}")
	private String email; // 電子郵件
	
	private Boolean active; // 帳號啟動
	
	private String role; // 角色權限
}
