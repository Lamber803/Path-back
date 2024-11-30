package com.example.demo.controller;

import com.example.demo.unit.JwtUtil;
import com.example.demo.service.UserService;
import com.example.demo.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // 用戶登入
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody com.example.demo.model.dto.RegistAndLoginRequest loginRequest) {
        try {
            // 创建认证令牌
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            // 通过 AuthenticationManager 进行认证
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 如果认证成功，获取认证信息
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 生成 JWT Token
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

            // 返回 token 给客户端
            return ResponseEntity.ok("Bearer " + jwtToken);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }
    }
}
