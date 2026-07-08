// 宣告這個 Java 檔案所在的資料夾路徑（訂單模組的 entity 套件）
package com.example.shopspringboot.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// 告訴 Spring Boot：這是一個 JPA 實體類別，對應資料庫的一張資料表
@Entity
// 指定對應的資料庫資料表名稱為 "orders"
@Table(name = "orders")
// Lombok 註解：自動生成 Getter、Setter、toString 等方法
@Data
public class OrderEntity {
    // 設定主鍵（Primary Key）
    @Id
    // 設定主鍵為資料庫內建的「自動遞增」（Auto Increment）
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 映射到資料庫的 "user_id" 欄位，且不能為空
    @Column(name = "user_id", nullable = false)
    private Long userId;
    // 映射到總金額欄位，電商金流一律使用 BigDecimal 避免浮點數失真，不能為空
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
    // 訂單狀態（例如：PENDING-待付款, PAID-已付款, CANCELLED-已取消）
    @Column(nullable = false)
    private String status;
    // 訂單建立時間
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}