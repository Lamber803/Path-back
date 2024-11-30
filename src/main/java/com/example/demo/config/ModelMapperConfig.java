package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Springboot 在啟動完成前會先行執行此配置
public class ModelMapperConfig {
	@Bean //將返回值註冊為Bean，其他地方可以透過依賴注入使用
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
