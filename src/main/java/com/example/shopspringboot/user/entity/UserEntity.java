package com.example.shopspringboot.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_table") // user 在 MySQL 是保留字，所以我們將表名取為 user_table
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) // 帳號不能重複
    private String username;
    @Column(nullable = false)
    private String password; // 這裡將來會存加密後的密碼（密文），絕對不存明文
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;
}