package com.example.demo.service;

import com.example.demo.model.dto.TodoDTO;
import com.example.demo.model.entity.Todo;
import com.example.demo.model.entity.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    // 新增待辦事項
    public Todo saveTodo(TodoDTO todoDTO) {
        Optional<User> userOptional = userRepository.findById(todoDTO.getUserId());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("使用者不存在");
        }

        User user = userOptional.get();
        Todo todo = new Todo();
        todo.setTodoText(todoDTO.getTodoText());
        todo.setIsCompleted(false); // 預設未完成
        todo.setUser(user);

        return todoRepository.save(todo);
    }

    // 修改待辦事項文本
    public Todo updateTodoText(TodoDTO todoDTO) {
        Optional<Todo> todoOptional = todoRepository.findById(todoDTO.getTodoId());
        if (!todoOptional.isPresent()) {
            throw new RuntimeException("待辦事項不存在");
        }

        Todo todo = todoOptional.get();
        todo.setTodoText(todoDTO.getTodoText());
        

        return todoRepository.save(todo);
    }

    // 完成待辦事項
    public Todo completeTodo(Integer todoId,boolean isCompleted) {
        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        if (!todoOptional.isPresent()) {
            throw new RuntimeException("待辦事項不存在");
        }

        Todo todo = todoOptional.get();
        todo.setIsCompleted(isCompleted);
        

        return todoRepository.save(todo);
    }

    // 刪除待辦事項
    public void deleteTodo(Integer todoId) {
        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        if (!todoOptional.isPresent()) {
            throw new RuntimeException("待辦事項不存在");
        }

        todoRepository.delete(todoOptional.get());
    }

    // 查詢所有待辦事項
    public List<TodoDTO> getTodosByUserId(Integer userId) {
        List<Todo> todos = todoRepository.findByUser_UserId(userId);
        return todos.stream()
                .map(todo -> new TodoDTO(
                        todo.getTodoId(),
                        todo.getUser().getUserId(),
                        todo.getTodoText(),
                        todo.getIsCompleted()
                ))
                .collect(Collectors.toList());
    }

    // 查詢某一待辦事項
    public TodoDTO getTodoById(Integer todoId) {
        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        if (!todoOptional.isPresent()) {
            throw new RuntimeException("待辦事項不存在");
        }

        Todo todo = todoOptional.get();
        return new TodoDTO(
                todo.getTodoId(),
                todo.getUser().getUserId(),
                todo.getTodoText(),
                todo.getIsCompleted()
        );
    }
}
