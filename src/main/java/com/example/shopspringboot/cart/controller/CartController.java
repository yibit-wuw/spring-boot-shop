package com.example.shopspringboot.cart.controller;

import com.example.shopspringboot.cart.dto.AddToCartRequest;
import com.example.shopspringboot.cart.dto.CartResponse;
import com.example.shopspringboot.cart.service.CartService;
import com.example.shopspringboot.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    // 處理「加入購物車」的 POST 請求
    @PostMapping
    public ApiResponse<Void> addToCart(@Valid @RequestBody AddToCartRequest request) {
        // 這裡暫時先寫死 userId = 1L（等串接 JWT Filter 後就能動態獲取）
        Long mockUserId = 1L;
        // 1. 【呼叫 Service】把資料傳給內廚執行（你寫得完全正確！）
        cartService.addToCart(mockUserId, request.getProductId(), request.getQuantity());
        // 2. 【回傳結果】直接 return 成功的回應格式給前端
        return ApiResponse.success("成功加入購物車");
    }
    @GetMapping
    public ApiResponse<List<CartResponse>> getCart() {
        // 依然先寫死模擬的使用者 ID
        Long mockUserId = 1L;
        // 1. 【呼叫 Service】拿著 mockUserId 去叫內廚把整袋 DTO 列表拿過來
        // 呼叫 Service 的查詢方法，並用一個 List<CartResponse> 變數接住回傳值（你寫得超棒！）
        List<CartResponse> responseList = cartService.getCartByUserId(mockUserId);
        // 2. 【回傳結果】把這袋資料和成功訊息用 ApiResponse 包起來，直接 return 給前端
        // 把原本的 null 拿掉，這樣就完全符合 Java 語法了！
        return ApiResponse.success("查詢成功", responseList);
    }
    // 處理「移出購物車」的 DELETE 請求
    @DeleteMapping("/{productId}")
    public ApiResponse<Void> removeFromCart(@PathVariable Long productId) {
        // 依然先寫死模擬的使用者 ID
        Long mockUserId = 1L;
        // 1. 【呼叫 Service】叫內廚執行刪除
        cartService.removeFromCart(mockUserId, productId);
        // 2. 【回傳結果】回傳 ApiResponse.success("成功移出購物車")
        return ApiResponse.success("成功移出購物車");
    }
}