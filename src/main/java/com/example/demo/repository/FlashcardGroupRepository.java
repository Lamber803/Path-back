package com.example.demo.repository;

import com.example.demo.model.entity.FlashcardGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashcardGroupRepository extends JpaRepository<FlashcardGroup, Integer> {

    // 查詢用戶的所有字卡組
    List<FlashcardGroup> findByUser_UserId(Integer userId);

    // 查詢特定字卡組
    Optional<FlashcardGroup> findByGroupId(Integer groupId);
}
