package com.example.demo.repository;

import com.example.demo.model.entity.FlashcardGroupFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardGroupFavoriteRepository extends JpaRepository<FlashcardGroupFavorite, Integer> {

    // 查詢特定字卡組的所有收藏字卡
    List<FlashcardGroupFavorite> findByFlashcardGroup_GroupId(Integer groupId);

    // 查詢某個字卡是否在特定字卡組的收藏庫內
    boolean existsByFlashcardGroup_GroupIdAndFlashcard_FlashcardId(Integer groupId, Integer flashcardId);
}
