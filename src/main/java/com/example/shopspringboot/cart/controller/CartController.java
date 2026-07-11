package com.example.shopspringboot.cart.controller;

import com.example.shopspringboot.cart.dto.AddToCartRequest;
import com.example.shopspringboot.cart.dto.CartResponse;
import com.example.shopspringboot.cart.service.CartService;
import com.example.shopspringboot.dto.ApiResponse;
import com.example.shopspringboot.user.service.UserService; // 💡 1. 引入 UserService
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService; // 💡 2. 宣告 UserService
    // 💡 3. 透過建構子將兩個 Service 一起注入進來
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }
    // 處理「加入購物車」的 POST 請求
    @PostMapping
    public ApiResponse<Void> addToCart(@Valid @RequestBody AddToCartRequest request) {
        // 💡 4. 拔掉死資料，改從 JWT 憑證動態解析出當前用戶 ID
        Long userId = userService.getCurrentUserId();
        // 將動態 ID 傳給 Service 執行
        cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return ApiResponse.success("成功加入購物車");
    }
    // 處理「查詢購物車」的 GET 請求
    @GetMapping
    public ApiResponse<List<CartResponse>> getCart() {
        // 💡 5. 動態獲取登入者的 ID
        Long userId = userService.getCurrentUserId();
        // 拿著當前用戶的 ID 去撈他個人的購物車內容
        List<CartResponse> responseList = cartService.getCartByUserId(userId);
        return ApiResponse.success("查詢成功", responseList);
    }
    // 處理「移出購物車」的 DELETE 請求
    @DeleteMapping("/{productId}")
    public ApiResponse<Void> removeFromCart(@PathVariable Long productId) {
        // 💡 6. 動態獲取登入者的 ID（順便把原本混進來的訂單邏輯清乾淨了！）
        Long userId = userService.getCurrentUserId();
        // 叫 Service 把該用戶購物車裡的特定商品移除
        cartService.removeFromCart(userId, productId);
        return ApiResponse.success("成功移出購物車");
    }
}