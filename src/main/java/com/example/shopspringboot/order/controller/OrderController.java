package com.example.shopspringboot.order.controller;

import com.example.shopspringboot.dto.ApiResponse; // 引用你專案原有的統一回應格式
import com.example.shopspringboot.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders") // 這一組網址開頭都是 /api/orders
public class OrderController {

    private final OrderService orderService;
    // 透過建構子注入寫好的 OrderService
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    // 處理「去結帳/建立訂單」的 POST 請求
    @PostMapping
    public ApiResponse<Void> createOrder() {
        // 這裡依然先寫死模擬的使用者 ID = 1L（等以後串接 JWT 後再改為動態獲取）
        Long mockUserId = 1L;
        // 1. 【呼叫 Service】叫內廚執行最核心的結帳一條龍服務
        //（包含查購物車、扣庫存、算總價、存主表、存明細、清空購物車）
        orderService.createOrder(mockUserId);
        // 2. 【回傳結果】當上面那行順利跑完，代表結帳成功，直接 return 成功回應給前端
        return ApiResponse.success("訂單建立成功，請前往付款");
    }
}