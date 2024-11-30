package com.example.demo.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主鍵自動生成，使用資料庫的自增特性
    @Column(name = "user_id")
    private Integer userId; // 使用者ID

    @Column(name = "user_name", nullable = false, unique = true)  // 使用者名稱，不能為空，並且唯一
    private String username; // 使用者名稱

    @Column(name = "password_hash", nullable = false, columnDefinition = "varchar(255)")  // 使用者的 Hash 密碼
    private String passwordHash;

    @Column(name = "salt", nullable = false, columnDefinition = "varchar(255)")  // 隨機鹽
    private String salt;

    @Column(name = "email", columnDefinition = "varchar(255)")  // 電子郵件
    private String email;

    @Column(name = "active", nullable = false)  // 帳號啟動，預設值應該由資料庫提供
    private Boolean active;

    @Column(name = "role", nullable = false, columnDefinition = "varchar(50) default 'ROLE_USER'")  // 預設角色
    private String role;
}
