package com.example.shopspringboot.user.repository;

import com.example.shopspringboot.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 這是「會員資料庫」的連線控制介面（Repository）。
 * 想像它是專門管理資料庫中 User（會員）這張資料表的「倉庫管理員」。
 *
 * 這裡繼承了 JpaRepository，代表它自動內建了常見的 CRUD 功能（新增、查詢、修改、刪除），
 * 我們不需要自己辛苦寫一堆 SQL 指令（像是 SELECT * FROM user ...）。
 *
 * JpaRepository<User, Long> 的意思是：
 * - User: 這個倉庫主要存放的「食材/資料格式」是 User 實體。
 * - Long: User 資料表裡面的主鍵（ID）資料型態是長整數（Long）。
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 【依帳號查詢會員】
     *
     * 作用：當使用者要登入時，櫃檯（Controller）會問主廚（Service）：「有沒有這個人的帳號？」
     *       主廚就會來叫倉庫管理員執行這個方法。
     *
     * 幕後原理：Spring Boot 看到 findByUsername，會自動轉成 SQL 語法：
     *          "SELECT * FROM user WHERE username = 傳入的帳號;"
     *
     * 為什麼用 Optional<User>？
     * Optional 就像是一個「保險箱」，裡面可能「有 User」或者「是空的（Null）」。
     * 這樣可以強迫後端程式碼在拿資料時，先檢查這個帳號到底存不存在，避免程式因為找不到人而崩潰（NullPointerException）。
     */
    Optional<User> findByUsername(String username);

    /**
     * 【依 Email 查詢會員】
     *
     * 作用：當新使用者註冊時，主廚（Service）需要檢查這個 Email 是不是已經被別人註冊過了。
     *       這時候就會呼叫這個方法。
     *
     * 幕後原理：自動轉成 SQL 語法：
     *          "SELECT * FROM user WHERE email = 傳入的 Email;"
     */
    Optional<User> findByEmail(String email);
}