package com.example.shopspringboot.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// Lombok 註解：自動生成這兩個欄位的 Getter、Setter 等方法
@Data
public class AddToCartRequest {

    // 前端必須傳過來的商品 ID，絕對不能是空的
    @NotNull(message = "商品 ID 不能為空")
    private Long productId;

    // 前端必須傳過來的購買數量，不能為空且最小必須是 1
    @NotNull(message = "購買數量不能為空")
    @Min(value = 1, message = "購買數量至少需要為 1")
    private Integer quantity;
}