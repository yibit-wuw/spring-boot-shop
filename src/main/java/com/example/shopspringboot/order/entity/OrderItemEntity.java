// 宣告這個 Java 檔案所在的資料夾路徑
package com.example.shopspringboot.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
// 告訴 Spring Boot：這是一個 JPA 實體類別，對應資料庫的一張資料表
@Entity
// 指定對應的資料庫資料表名稱為 "order_items"
@Table(name = "order_items")
// Lombok 註解：自動生成 Getter、Setter、toString 等方法
@Data
public class OrderItemEntity {
    // 設定明細表自己的主鍵（Primary Key）
    @Id
    // 設定主鍵為資料庫內建的「自動遞增」（Auto Increment）
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 對應訂單主表的 id，用來識別這筆明細屬於哪張訂單
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    // 對應商品的 id，用來識別買了哪樣商品
    @Column(name = "product_id", nullable = false)
    private Long productId;
    // 紀錄購買「當下」的商品單價。一定要記，因為未來商品可能會漲價或打折！
    @Column(nullable = false)
    private BigDecimal price;
    // 購買數量
    @Column(nullable = false)
    private Integer quantity;
}