package com.example.shopspringboot.product.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data // Lombok 註解：自動幫你生成所有 Getter, Setter 和 toString
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主鍵自動遞增
    private Long id;
    @Column(nullable = false)
    private String name;        // 商品名稱
    private String description; // 商品描述
    @Column(nullable = false)
    private BigDecimal price;   // 商品價格（商城專案金錢一律用 BigDecimal 防止小數點精度遺失）
    @Column(nullable = false)
    private Integer stock;      // 商品庫存
    @Column(name = "image_url")
    private String imageUrl;

}
