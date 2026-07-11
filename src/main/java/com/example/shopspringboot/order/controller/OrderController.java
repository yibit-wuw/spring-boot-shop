package com.example.shopspringboot.order.controller;

import com.example.shopspringboot.dto.ApiResponse;
import com.example.shopspringboot.dto.PageResponse;
import com.example.shopspringboot.order.dto.CreateOrderResponse;
import com.example.shopspringboot.order.dto.OrderResponse;
import com.example.shopspringboot.order.service.OrderService;
import com.example.shopspringboot.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<CreateOrderResponse> createOrder() {
        Long currentUserId = userService.getCurrentUserId();
        Long orderId = orderService.createOrder(currentUserId);
        return ApiResponse.success("訂單建立成功，請前往付款", new CreateOrderResponse(orderId));
    }

    @GetMapping
    public ApiResponse<PageResponse<OrderResponse>> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Long currentUserId = userService.getCurrentUserId();
        PageResponse<OrderResponse> orders = orderService.getOrdersByUserId(currentUserId, page, size);
        return ApiResponse.success("查詢訂單列表成功", orders);
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        OrderResponse order = orderService.getOrderById(currentUserId, id);
        return ApiResponse.success("查詢訂單成功", order);
    }

    @PostMapping("/{id}/pay")
    public ApiResponse<Void> payOrder(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        orderService.payOrder(currentUserId, id);
        return ApiResponse.success("付款成功");
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelOrder(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        orderService.cancelOrder(currentUserId, id);
        return ApiResponse.success("訂單已取消");
    }
}
