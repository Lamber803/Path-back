package com.example.demo.repository;

import com.example.demo.model.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUser_UserId(Integer userId);  // 查詢特定使用者的所有代辦事項
    Optional<Todo> findByTodoIdAndUser_UserId(Integer todoId, Integer userId);  // 查詢指定使用者的特定代辦
}
