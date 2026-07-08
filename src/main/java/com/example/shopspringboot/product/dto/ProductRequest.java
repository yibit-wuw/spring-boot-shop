package com.example.shopspringboot.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "商品名稱不能為空")
    private String name;
    private String description;
    @NotNull(message = "商品價格不能為空")
    @Min(value = 0, message = "商品價格不能小於 0")
    private BigDecimal price;
    @NotNull(message = "商品庫存不能為空")
    @Min(value = 0, message = "商品庫存不能小於 0")
    private Integer stock;

    // 💡 提示：上架時通常預設就是上架狀態（例如 status = "ACTIVE"），所以不用讓前端傳傳狀態。
}