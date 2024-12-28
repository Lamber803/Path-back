package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardGroupFavoriteDTO {

	private Integer favoriteId;  // 收藏ID
	private Integer flashcardId;  // 字卡ID
	private Integer groupId;  // 字卡組ID
}
