package com.example.demo.controller;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.ChangePasswordRequest;
import com.example.demo.model.dto.DeleteAccountRequest;
import com.example.demo.model.dto.RegistAndLoginRequest;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;
import com.example.demo.unit.JwtUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

 
@RestController
@RequestMapping("/api/users")

public class UserController {

	@Autowired
	private UserService userService;
    @Autowired
    private UserRepository userRepository;

 // 用戶註冊，密碼由前端傳遞，後端進行哈希處理
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegistAndLoginRequest userRegistrationRequest) {
        try {
            // 使用 service 層處理註冊，並將原始密碼傳遞過去
            User registeredUser = userService.registerUser(
                    userRegistrationRequest.getUsername(),
                    userRegistrationRequest.getPassword(),
                    userRegistrationRequest.getEmail(),
                    userRegistrationRequest.getRole()
            );
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            // 如果出現錯誤，返回 400 錯誤響應
            return ResponseEntity.badRequest().build();
        }
    }

    // 用戶登入
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginUser(@RequestBody RegistAndLoginRequest loginRequest) {
        try {
            // 使用 service 層進行登入邏輯
            userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
         // 生成 JWT Token
            String jwtToken = JwtUtil.generateToken(loginRequest.getUsername());

            // 返回 token 给客户端
            return ResponseEntity.ok(ApiResponse.success("登入成功",jwtToken));
        } catch (UserNotFoundException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(HttpStatus.NOT_FOUND.value(),"登入失敗"));
        }
        
        
    }
    
    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getUserIdByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId()); // 假設 User 物件有 userId 屬性
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

    // 變更密碼
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            // 使用 service 層處理變更密碼邏輯
            userService.changePassword(changePasswordRequest.getUsername(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
            return ResponseEntity.ok("Password changed successfully!");
        } catch (UserNotFoundException | RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }
    }

 // 刪除帳號
    @PostMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@RequestBody DeleteAccountRequest deleteAccountRequest) {
        try {
            // 驗證 'deleteToken' 是否為 "Delete"
            if (!"Delete".equals(deleteAccountRequest.getDeleteToken())) {
                return ResponseEntity.status(400).body("Invalid delete token.");
            }

            userService.deleteAccount(deleteAccountRequest.getUsername(), deleteAccountRequest.getPassword());
                return ResponseEntity.ok("User deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error deleting user: " + e.getMessage());
        }
    }
}

