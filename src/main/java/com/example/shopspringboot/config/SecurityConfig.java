package com.example.shopspringboot.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 啟用 CORS 並帶入我們的配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. 關閉 CSRF (因為我們使用 JWT，不需要 Session 餅乾防禦)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/products/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
    // 3. 定義 CORS 配置源
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允許前端 Vite 的預設網址
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // 允許常見的 HTTP 動作
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允許前端帶任何 Header 過去 (例如後續會用到的 Authorization)
        configuration.setAllowedHeaders(List.of("*"));
        // 允許攜帶認證資訊 (Cookie、認證 Header 等)
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 套用到所有 API 路徑
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/*這是一個配合JWT驗證 的安全規則檔案
告訴 Spring Security 如何保護你的 API、哪些路徑需要驗證。
目前/api/products/**", "/api/auth/**" 兩個入口不需要驗證就放行 不會被攔截
配置安全規則驗證身份的檔案


Spring Security核心功能：負責全站的安全防護。
防範 CSRF 攻擊、密碼自動加密儲存。商城應用：區分角色權限。
例如：限制只有「管理員」才能上架商品（/admin/**），普通「會員」只能檢視訂單。

配置CORS
**/