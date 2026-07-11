package com.example.shopspringboot.config;

import com.example.shopspringboot.user.entity.UserEntity;
import com.example.shopspringboot.user.entity.UserRole;
import com.example.shopspringboot.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        userRepository.findByUsername(adminUsername).ifPresentOrElse(
                user -> {
                    if (user.getRole() != UserRole.ADMIN) {
                        user.setRole(UserRole.ADMIN);
                        userRepository.save(user);
                        log.info("已將既有帳號 {} 提升為 ADMIN", adminUsername);
                    }
                },
                () -> {
                    UserEntity admin = new UserEntity();
                    admin.setUsername(adminUsername);
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setEmail(adminUsername + "@shop.local");
                    admin.setRole(UserRole.ADMIN);
                    userRepository.save(admin);
                    log.info("已建立預設管理員帳號 {}", adminUsername);
                }
        );
    }
}
