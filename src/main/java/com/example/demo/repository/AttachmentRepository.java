package com.example.demo.repository;

import com.example.demo.model.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    // 你可以在这里定义其他自定义查询方法
}

