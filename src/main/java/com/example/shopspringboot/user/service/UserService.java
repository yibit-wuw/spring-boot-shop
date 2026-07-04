package com.example.shopspringboot.user.service;

import com.example.shopspringboot.user.entity.User;
import com.example.shopspringboot.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    // 引入 Spring Security 官方推薦的密碼加密工具
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    // 1. 會員註冊邏輯
    public String register(User user) {
        // 檢查帳號是否被註冊過
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "錯誤：該帳號已被註冊！";
        }
        // 將密碼進行哈希加密（例如 123456 會變成 $2a$10$... 這種長字串）
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        return "註冊成功！";
    }

    // 2. 會員登入邏輯（成功就回傳 JWT）
    public String login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return "錯誤：找不到該用戶！";
        }

        User user = userOpt.get();
        // 比對前端傳來的密碼與資料庫裡的加密密碼是否吻合
        if (passwordEncoder.matches(password, user.getPassword())) {
            // 密碼正確，發放 JWT Token 通行證
            return jwtTokenService.generateToken(user.getUsername());
        } else {
            return "錯誤：密碼不正確！";
        }
    }
}