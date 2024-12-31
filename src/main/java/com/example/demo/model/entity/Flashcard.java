package com.example.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Flashcard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flashcard_id")
    private Integer flashcardId;  // 字卡ID
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore // 忽略该字段的序列化
    private FlashcardGroup flashcardGroup;  // 該字卡屬於哪個字卡組
    
    @Column(name = "word", nullable = false, columnDefinition = "varchar(255)")
    private String word;  // 單字
    
    @Column(name = "definition", nullable = false, columnDefinition = "text")
    private String definition;  // 字卡的定義
    
    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite; // 表示字卡是否被標記為收藏
}
