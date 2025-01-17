package com.example.demo.repository;

import com.example.demo.model.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<Attachment, Integer> {
    List<Attachment> findByDocument_DocumentId(Integer documentId);  // 查詢特定文檔的所有檔案
}
