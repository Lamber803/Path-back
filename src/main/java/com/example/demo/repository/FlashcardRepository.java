package com.example.demo.repository;

import com.example.demo.model.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

    // 查詢特定字卡組下的所有字卡
    List<Flashcard> findByFlashcardGroup_GroupId(Integer groupId);

    // 查詢指定字卡的字卡
    Optional<Flashcard> findByFlashcardId(Integer flashcardId);
}
