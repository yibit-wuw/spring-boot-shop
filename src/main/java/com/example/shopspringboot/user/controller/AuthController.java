// 指定此類別所在的 package（套件）
package com.example.shopspringboot.user.controller;
// 匯入統一 API 回傳格式
import com.example.shopspringboot.dto.ApiResponse;
// 匯入登入請求 DTO（接收前端登入資料）
import com.example.shopspringboot.user.dto.LoginRequest;
// 匯入登入成功回傳 DTO
import com.example.shopspringboot.user.dto.LoginResponse;
// 匯入註冊請求 DTO（接收前端註冊資料）
import com.example.shopspringboot.user.dto.RegisterRequest;
// 匯入 User 實體(Entity)，代表資料庫的 users 資料
import com.example.shopspringboot.user.entity.User;
// 匯入 UserService，負責處理使用者相關商業邏輯
import com.example.shopspringboot.user.service.UserService;
// 匯入 Jakarta Validation，用來驗證 DTO 是否符合規則（例如 @NotBlank）
import jakarta.validation.Valid;
// 匯入 HTTP 狀態碼
import org.springframework.http.HttpStatus;
// 匯入 ResponseEntity，可以自訂 HTTP 狀態碼及回傳內容
import org.springframework.http.ResponseEntity;
// 匯入 PostMapping，表示此方法處理 POST 請求
import org.springframework.web.bind.annotation.PostMapping;
// 匯入 RequestBody，表示接收 Request Body 的 JSON 資料
import org.springframework.web.bind.annotation.RequestBody;
// 匯入 RequestMapping，設定 Controller 的共同路徑
import org.springframework.web.bind.annotation.RequestMapping;
// 匯入 RestController，表示此類別是一個 REST API Controller
import org.springframework.web.bind.annotation.RestController;
// 宣告這是一個 REST Controller，所有方法都會直接回傳 JSON
@RestController
// 設定此 Controller 的共同網址路徑
// 所有 API 都會以 /api/auth 開頭
@RequestMapping("/api/auth")
public class AuthController {
    // 宣告 UserService 物件
    // final 表示建立後不能再被修改
    private final UserService userService;
    // 建構子注入（Constructor Injection）
    // Spring Boot 會自動把 UserService 注入進來
    public AuthController(UserService userService) {
        // 將注入的 UserService 指派給成員變數
        this.userService = userService;
    }
    // 建立 POST API
    // 完整路徑：POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            // @Valid 會先驗證 LoginRequest 是否符合驗證規則
            // @RequestBody 表示接收前端傳來的 JSON
            @Valid @RequestBody LoginRequest loginRequest) {
        // 使用 try-catch 處理登入可能發生的錯誤
        try {
            // 呼叫 Service 驗證帳號密碼
            // 如果成功會回傳 JWT Token
            String token = userService.login(
                    loginRequest.getUsername(),
                    loginRequest.getPassword());
            // 建立登入成功回傳物件
            LoginResponse loginResponse = new LoginResponse(
                    token,
                    loginRequest.getUsername());
            // 回傳 HTTP 200 OK
            // ApiResponse.success() 建立成功格式
            return ResponseEntity.ok(
                    ApiResponse.success("登入成功", loginResponse));
        } catch (RuntimeException e) {
            // 如果登入失敗（帳號或密碼錯誤）
            // 回傳 HTTP 401 Unauthorized
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    // 建立 POST API
    // 完整路徑：POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            // 驗證 RegisterRequest 是否符合規則
            @Valid @RequestBody RegisterRequest registerRequest) {
        // 使用 try-catch 處理註冊錯誤
        try {
            // 建立新的 User Entity
            User user = new User();
            // 將 DTO 的 username 放到 User Entity
            user.setUsername(registerRequest.getUsername());
            // 將 DTO 的 password 放到 User Entity
            user.setPassword(registerRequest.getPassword());
            // 將 DTO 的 email 放到 User Entity
            user.setEmail(registerRequest.getEmail());
            // 呼叫 Service 進行註冊
            // Service 裡通常會檢查帳號是否重複、密碼加密、存入資料庫
            userService.register(user);
            // 註冊成功
            // 回傳 HTTP 200
            return ResponseEntity.ok(
                    ApiResponse.success("註冊成功"));
        } catch (RuntimeException e) {
            // 如果註冊失敗（例如帳號重複）
            // 回傳 HTTP 400 Bad Request
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}