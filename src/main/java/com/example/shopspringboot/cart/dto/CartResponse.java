package com.example.shopspringboot.cart.dto;

import lombok.Data;

@Data // Lombok 自動生成 Getter / Setter
public class CartResponse {

    // 儲存使用者的 ID
    private Long userId;

    // 儲存商品的 ID
    private Long productId;

    // 儲存該商品被加入購物車的數量
    private Integer quantity;
}