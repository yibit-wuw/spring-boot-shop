package com.example.shopspringboot.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
// @Data
// 等同於自動產生：
// 1. getter
// 2. setter
// 3. toString()
// 4. equals()
// 5. hashCode()
@Data
// RegisterRequest 類別
// 用來接收前端註冊時送來的 JSON 資料
public class RegisterRequest {
    // @NotBlank
    // 驗證使用者名稱不能為 null、空字串("") 或只有空白("   ")
    // 如果驗證失敗，會回傳指定的錯誤訊息
    @NotBlank(message = "請輸入使用者名稱")
    // @Size
    // 驗證使用者名稱長度必須介於 3 到 50 個字元之間
    // min = 最少 3 個字
    // max = 最多 50 個字
    @Size(min = 3, max = 50, message = "使用者名稱長度需為 3～50 字")
    // 儲存使用者名稱
    private String username;
    // @NotBlank
    // 密碼不能為空
    @NotBlank(message = "請輸入密碼")
    // @Size
    // 密碼至少要 6 個字元
    // 最多 100 個字元
    @Size(min = 6, max = 100, message = "密碼長度至少 6 字")
    // 儲存密碼
    private String password;
    // @NotBlank
    // Email 不可以空白
    @NotBlank(message = "請輸入 Email")
    // @Email
    // 驗證是否符合 Email 格式
    // 例如：
    // abc@gmail.com    ✔
    // test@yahoo.com   ✔
    // abc123           ✘
    // gmail.com        ✘
    @Email(message = "Email 格式不正確")
    // 儲存 Email
    private String email;
}
