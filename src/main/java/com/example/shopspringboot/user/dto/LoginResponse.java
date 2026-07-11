// 指定此類別所在的 package（套件）
package com.example.shopspringboot.user.dto;
// 匯入 Lombok 的 @AllArgsConstructor
// 自動產生「包含所有屬性的建構子」
import lombok.AllArgsConstructor;
// 匯入 Lombok 的 @Data
// 自動產生 Getter、Setter、toString()、equals()、hashCode()
import lombok.Data;
// 匯入 Lombok 的 @NoArgsConstructor
// 自動產生無參數建構子
import lombok.NoArgsConstructor;
// @Data
// 等同於自動產生：
// 1. getter
// 2. setter
// 3. toString()
// 4. equals()
// 5. hashCode()
@Data
// 自動產生沒有參數的建構子
// 等同於：public LoginResponse(){}
@NoArgsConstructor
// 自動產生包含所有屬性的建構子
// 等同於：
// public LoginResponse(String token, String username){...}
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String role;
}