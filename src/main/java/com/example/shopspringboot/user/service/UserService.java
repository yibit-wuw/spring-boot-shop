package com.example.shopspringboot.user.service;

import com.example.shopspringboot.user.entity.User;
import com.example.shopspringboot.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
// @Service
// 告訴 Spring 這是一個 Service 類別
// Spring 啟動時會自動建立一個 UserService 物件（Bean）並交由 Spring 管理
@Service
public class UserService {
    // 宣告 UserRepository
    // 負責與資料庫(users 資料表)進行操作
    // final 表示建立後不能再指向其他物件
    private final UserRepository userRepository;
    // 宣告 JwtTokenService
    // 負責產生、解析、驗證 JWT Token
    private final JwtTokenService jwtTokenService;
    // 建立 BCryptPasswordEncoder 物件
    // 用來將密碼加密(Hash)以及驗證密碼
    // 例如：
    // encode("123456") → "$2a$10$..."
    // matches("123456", 加密後密碼) → true
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // 建構子（Constructor）
    // Spring Boot 會自動把 UserRepository 與 JwtTokenService 注入進來
    public UserService(UserRepository userRepository, JwtTokenService jwtTokenService) {
        // 將注入的 UserRepository 指派給成員變數
        this.userRepository = userRepository;
        // 將注入的 JwtTokenService 指派給成員變數
        this.jwtTokenService = jwtTokenService;
    }
    // 1. 會員註冊邏輯
    public User register(User user) {
        // 檢查帳號是否被註冊過
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("該帳號已被註冊！");
        }
        // 【新增】檢查 Email 是否被註冊過 (需要在 UserRepository 補上 findByEmail 方法)
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("該 Email 已被使用！");
        }
        // 將密碼進行哈希加密
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        // 儲存並回傳儲存後的 User 物件
        return userRepository.save(user);
    }
    // 2. 會員登入邏輯（成功就回傳 JWT）
    public String login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("找不到該用戶！");
        }
        User user = userOpt.get();
        // 比對前端傳來的密碼與資料庫裡的加密密碼是否吻合
        if (passwordEncoder.matches(password, user.getPassword())) {
            // 密碼正確，發放 JWT Token 通行證
            return jwtTokenService.generateToken(user.getUsername());
        } else {
            throw new RuntimeException("密碼不正確！");
        }
    }
}