package com.example.shopspringboot.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
// @Data
// 等同於自動產生：
// 1. getter
// 2. setter
// 3. toString()
// 4. equals()
// 5. hashCode()
@Data
public class LoginRequest {

    @NotBlank(message = "請輸入帳號")
    private String username;

    @NotBlank(message = "請輸入密碼")
    private String password;
}
