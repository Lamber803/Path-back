package com.example.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DocumentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;  // 群組ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // 忽略該字段的序列化
    private User user;  // 群組創建者，與 `User` 一對多關聯

    @Column(name = "group_name", nullable = false, columnDefinition = "varchar(255)")
    private String groupName;  // 群組名稱

    @OneToMany(mappedBy = "documentGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Document> documents;  // 群組包含的文檔列表
}
