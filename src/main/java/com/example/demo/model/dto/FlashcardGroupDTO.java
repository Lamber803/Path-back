package com.example.demo.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardGroupDTO {

	private Integer groupId;  // 字卡組ID
	private Integer userId;
	
	private String groupName;  // 字卡組名稱
	private List<FlashcardDTO> flashcards;  // 該字卡組內的所有字卡
}
