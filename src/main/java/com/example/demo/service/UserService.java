package com.example.demo.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.unit.Hash;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; //用戶倉庫，查詢和保存用戶
    
    // 用戶註冊方法
    public User registerUser(String username, String password, String email, String role) {
        // 檢查用戶名是否已經存在
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }

        // 創建隨機鹽
        String salt = Hash.getSalt();
        
        // 對密碼進行加鹽並加密
        String passwordHash = Hash.getHash(password, salt);
        
        // 創建新的用戶對象並填充資料
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setSalt(salt);
        user.setEmail(email);
        user.setActive(false);  // 默認帳號不啟用
        user.setRole(role != null ? role : "ROLE_USER");  // 默認角色為 USER
        
        // 保存用戶到資料庫
        return userRepository.save(user);
    }

 // 用戶登入
    public void loginUser(String username, String password) throws UserNotFoundException {
        // 根據使用者名稱查詢用戶資料
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 用查詢出來的 salt 對使用者輸入的密碼進行哈希
        String passwordHash = Hash.getHash(password, user.getSalt());

        // 比較哈希值
        if (!passwordHash.equals(user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        // 登入成功，無需返回值
    }
    

    // 根據用戶名查找用戶
    public User getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found"));
    }
    
 // 變更密碼
    public void changePassword(String username, String oldPassword, String newPassword) throws UserNotFoundException {
        // 根據使用者名稱查詢使用者
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 用查詢出來的 salt 對舊密碼進行哈希
        String oldPasswordHash = Hash.getHash(oldPassword, user.getSalt());

        // 比對舊密碼的哈希值是否匹配
        if (!oldPasswordHash.equals(user.getPasswordHash())) {
            throw new RuntimeException("Invalid old password");
        }

        // 生成新的鹽
        String newSalt = Hash.getSalt();
        // 用新的鹽和新密碼生成新的哈希
        String newPasswordHash = Hash.getHash(newPassword, newSalt);

        // 更新資料庫中的密碼哈希值和鹽
        user.setPasswordHash(newPasswordHash);
        user.setSalt(newSalt);

        // 保存變更後的使用者資料
        userRepository.save(user);
    }
 
    // 刪除帳號
    public void deleteAccount(String username, String password) throws UserNotFoundException {
        // 根據使用者名稱查詢使用者
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 用查詢出來的 salt 對密碼進行哈希
        String passwordHash = Hash.getHash(password, user.getSalt());

        // 比對密碼的哈希值是否匹配
        if (!passwordHash.equals(user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        // 刪除使用者帳號
        userRepository.delete(user);
    }
    
}

