package com.example.demo.controller;

import com.example.demo.model.dto.TodoDTO;
import com.example.demo.model.entity.Todo;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    // 新增待辦事項
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody TodoDTO todoDTO) {
        Todo todo = todoService.saveTodo(todoDTO);
        return ResponseEntity.ok(todo);
    }

    // 修改待辦事項文本
    @PutMapping
    public ResponseEntity<Todo> updateTodoText(@RequestBody TodoDTO todoDTO) {
        Todo todo = todoService.updateTodoText(todoDTO);
        return ResponseEntity.ok(todo);
    }

    // 完成待辦事項
    @PutMapping("/complete")
    public ResponseEntity<Todo> completeTodo(@RequestBody TodoDTO todoDTO) {
        Todo todo = todoService.completeTodo(todoDTO.getTodoId(), todoDTO.getIsCompleted());
        return ResponseEntity.ok(todo);
    }

    // 刪除待辦事項
    @DeleteMapping
    public ResponseEntity<Void> deleteTodo(@RequestBody Integer todoId) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    // 查詢某個使用者的所有待辦事項
    @GetMapping("/user")
    public ResponseEntity<List<TodoDTO>> getTodosByUserId(@RequestParam Integer userId) {
        List<TodoDTO> todos = todoService.getTodosByUserId(userId);
        return ResponseEntity.ok(todos);
    }

    // 查詢單一待辦事項
    @GetMapping("/{todoId}")
    public ResponseEntity<TodoDTO> getTodoById(@RequestParam Integer todoId) {
        TodoDTO todoDTO = todoService.getTodoById(todoId);
        return ResponseEntity.ok(todoDTO);
    }
}
