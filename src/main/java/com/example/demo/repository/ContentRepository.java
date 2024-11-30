package com.example.demo.repository;

import com.example.demo.model.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    Optional<Content> findByDocument_DocumentId(Integer documentId);  // 通過文檔 ID 查詢內容
}
