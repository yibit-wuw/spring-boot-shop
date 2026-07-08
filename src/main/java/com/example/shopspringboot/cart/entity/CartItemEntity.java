package com.example.shopspringboot.cart.entity;

import jakarta.persistence.*;
import lombok.Data;

// 告訴 Spring Boot 框架：這是一個 JPA 實體類別（Entity），它會對應到資料庫的一張資料表
@Entity
// 指定這個實體類別對應的資料庫資料表名稱為 "cart_items"
@Table(name = "cart_items")
// Lombok 註解：在編譯時自動幫這個類別生成 Getter、Setter、toString()、equals() 和 hashCode() 方法
@Data
public class CartItemEntity {
    // 告訴資料庫：這個欄位是這張表的主鍵（Primary Key）
    @Id
    // 設定主鍵的生成策略：使用資料庫內建的「自動遞增」（Auto Increment）機制
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 宣告主鍵欄位，對應資料庫的 BIGINT 型態，變數名稱為 id
    private Long id;
    // 映射到資料庫中名為 "user_id" 的欄位，並設定此欄位在資料庫中「不能為空」（NOT NULL）
    @Column(name = "user_id", nullable = false)
    // 儲存使用者的 ID（對應 users 表的 id）
    private Long userId;
    // 映射到資料庫中名為 "product_id" 的欄位，並設定此欄位在資料庫中「不能為空」（NOT NULL）
    @Column(name = "product_id", nullable = false)
    // 儲存商品的 ID（對應 products 表的 id）
    private Long productId;
    // 映射到資料庫中同名的 quantity 欄位，並設定此欄位在資料庫中「不能為空」（NOT NULL）
    @Column(nullable = false)
    // 儲存該商品被加入購物車的數量
    private Integer quantity;
}