package com.example.shopspringboot.user.repository;

import com.example.shopspringboot.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // JPA 的神奇魔法：只要按照規範命名，它會自動幫你寫好 "SELECT * FROM user_table WHERE username = ?" 的 SQL
    Optional<User> findByUsername(String username);
}