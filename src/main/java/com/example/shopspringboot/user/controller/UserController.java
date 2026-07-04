package com.example.shopspringboot.user.controller;

import com.example.shopspringboot.user.entity.User;
import com.example.shopspringboot.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // 註冊登入的基礎網址
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 註冊 API (前端用 POST 傳送 user 的 JSON 資料)
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    // 登入 API (前端傳送 user 資料，成功會拿到一串很長的 Token)
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }
}