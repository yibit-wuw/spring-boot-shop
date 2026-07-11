package com.example.shopspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // 1. Key 序列化：依然使用標準的字串序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // 2. 💡 核心改變：新版 Redis 4.0 官方推薦直接呼叫 RedisSerializer.json()
        // 這會全自動幫你配置好最安全、支援 Java 8 時間、且沒有漏洞風險的 Jackson 序列化器
        RedisSerializer<Object> jsonSerializer = RedisSerializer.json();
        // 3. Value 序列化：使用官方推薦的全新 jsonSerializer
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }
}