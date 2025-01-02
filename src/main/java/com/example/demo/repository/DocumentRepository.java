package com.example.demo.repository;

import com.example.demo.model.entity.Document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByUser_UserId(Integer userId);  // 查詢特定使用者的所有文檔
    
 // 查詢特定字卡組下的所有字卡
    List<Document> findByDocumentGroup_GroupId(Integer groupId);
    
    Optional<Document> findByDocumentIdAndUser_UserId(Integer documentId, Integer userId);  // 查詢指定使用者的特定文檔
    
    Optional<Document> findById(Integer documentId);  // 查詢特定文檔
}
