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
    private Boolean isFavorite; // 表示字卡是否被標記為收藏

    public FlashcardDTO(Integer groupId, String word, String definition,Boolean isFavorite) {
        this.groupId = groupId;
        this.word = word;
        this.definition = definition;
        this.isFavorite = isFavorite;
    }

    public FlashcardDTO(Integer flashcardId, Boolean isFavorite) {
        this.flashcardId = flashcardId;
        this.isFavorite = isFavorite;
    }
}
