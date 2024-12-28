package com.example.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FlashcardGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;  // 字卡組ID
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // 忽略该字段的序列化
    private User user;  // 字卡組的創建者（即該字卡組屬於哪個用戶）

    @Column(name = "group_name", nullable = false, columnDefinition = "varchar(255)")
    private String groupName;  // 字卡組名稱
    
    @OneToMany(mappedBy = "flashcardGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Flashcard> flashcards;  // 該組別下的字卡
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "flashcard_group_favorites",  // 收藏庫的關聯表
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "flashcard_id")
    )
    @JsonIgnore // 忽略該字段的序列化
    private List<Flashcard> favoriteFlashcards;  // 收藏的字卡（來自該字卡組的字卡）
}
