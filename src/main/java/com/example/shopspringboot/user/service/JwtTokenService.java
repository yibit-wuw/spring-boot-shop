package com.example.shopspringboot.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value; // 記得引入這個
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtTokenService {

    // 透過 @Value 自動去 application.properties 抓取 jwt.secret 的值
    @Value("${jwt.secret}")
    private String secretKey;

    // 生成 Token 的方法
    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username) // 把用戶名包進 Token 裡
                .withIssuedAt(new Date()) // 發放時間
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 24小時後過期
                .sign(Algorithm.HMAC256(secretKey)); // 使用讀取進來的 secretKey 加密
    }
}