package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardDTO {

	private Integer flashcardId;  // 字卡ID
	private Integer groupId;  // 字卡組ID
	private String word;  // 單字
	private String definition;  // 字卡的定義
	
	public FlashcardDTO(Integer groupId, String word,String definition) {
		this.groupId = groupId;
		this.word = word;
		this.definition = definition;
	}
}
