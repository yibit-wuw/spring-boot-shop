package com.example.shopspringboot.user.service;

import com.example.shopspringboot.exception.BusinessException;
import com.example.shopspringboot.user.entity.UserEntity;
import com.example.shopspringboot.user.entity.UserRole;
import com.example.shopspringboot.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public void register(UserEntity user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BusinessException("該帳號已被註冊！");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("該 Email 已被使用！");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    public String login(String username, String password) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new BusinessException("帳號或密碼不正確！");
        }
        UserEntity user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenService.generateToken(user.getUsername(), user.getRole());
        }
        throw new BusinessException("帳號或密碼不正確！");
    }

    public UserRole getUserRole(String username) {
        return userRepository.findByUsername(username)
                .map(UserEntity::getRole)
                .orElse(UserRole.USER);
    }

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String username) {
            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BusinessException("當前登入效期異常，找不到該用戶"));
            return user.getId();
        }
        throw new BusinessException("用戶未登入，請先登入");
    }
}
