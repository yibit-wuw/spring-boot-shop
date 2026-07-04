package com.example.shopspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 關閉 CSRF 防護
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/products/**", "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
/*這是一個配合JWT驗證 的安全規則檔案
告訴 Spring Security 如何保護你的 API、哪些路徑需要驗證。
目前/api/products/**", "/api/auth/**" 兩個入口不需要驗證就放行 不會被攔截
配置安全規則驗證身份的檔案


Spring Security核心功能：負責全站的安全防護。
防範 CSRF 攻擊、密碼自動加密儲存。商城應用：區分角色權限。
例如：限制只有「管理員」才能上架商品（/admin/**），普通「會員」只能檢視訂單。
**/