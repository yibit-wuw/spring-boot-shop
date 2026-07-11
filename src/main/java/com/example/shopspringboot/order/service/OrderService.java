package com.example.shopspringboot.order.service;

import com.example.shopspringboot.cart.entity.CartItemEntity;
import com.example.shopspringboot.cart.repository.CartRepository;
import com.example.shopspringboot.dto.PageResponse;
import com.example.shopspringboot.exception.BusinessException;
import com.example.shopspringboot.order.dto.OrderItemResponse;
import com.example.shopspringboot.order.dto.OrderResponse;
import com.example.shopspringboot.order.entity.OrderEntity;
import com.example.shopspringboot.order.entity.OrderItemEntity;
import com.example.shopspringboot.order.repository.OrderItemRepository;
import com.example.shopspringboot.order.repository.OrderRepository;
import com.example.shopspringboot.product.entity.ProductEntity;
import com.example.shopspringboot.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        ProductRepository productRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Long createOrder(Long userId) {
        List<CartItemEntity> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new BusinessException("購物車是空的，無法結帳！");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItemEntity item : cartItems) {
            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new BusinessException("此商品已下架或不存在"));
            if (product.getStock() < item.getQuantity()) {
                throw new BusinessException("商品 " + product.getName() + " 庫存不足，無法結帳！");
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
            totalPrice = totalPrice.add(
                    product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        OrderEntity order = new OrderEntity();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        OrderEntity savedOrder = orderRepository.save(order);

        for (CartItemEntity item : cartItems) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new BusinessException("商品資料異常"));
            orderItem.setPrice(product.getPrice());
            orderItemRepository.save(orderItem);
        }

        cartRepository.deleteAll(cartItems);
        return savedOrder.getId();
    }

    @Transactional
    public void payOrder(Long userId, Long orderId) {
        OrderEntity order = getOwnedOrder(userId, orderId);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException("此訂單無法付款");
        }
        order.setStatus("PAID");
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        OrderEntity order = getOwnedOrder(userId, orderId);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException("此訂單無法取消");
        }

        List<OrderItemEntity> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItemEntity item : items) {
            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new BusinessException("商品資料異常"));
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    public PageResponse<OrderResponse> getOrdersByUserId(Long userId, int page, int size) {
        Page<OrderEntity> result = orderRepository.findByUserIdOrderByCreatedAtDesc(
                userId, PageRequest.of(page, size));
        List<OrderResponse> content = new ArrayList<>();
        for (OrderEntity order : result.getContent()) {
            content.add(toOrderResponse(order));
        }
        return new PageResponse<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    public OrderResponse getOrderById(Long userId, Long orderId) {
        return toOrderResponse(getOwnedOrder(userId, orderId));
    }

    private OrderEntity getOwnedOrder(Long userId, Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("找不到訂單"));
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("無權限操作此訂單");
        }
        return order;
    }

    private OrderResponse toOrderResponse(OrderEntity order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemEntity> items = orderItemRepository.findByOrderId(order.getId());
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItemEntity item : items) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setProductId(item.getProductId());
            itemResponse.setPrice(item.getPrice());
            itemResponse.setQuantity(item.getQuantity());
            productRepository.findById(item.getProductId())
                    .ifPresent(product -> itemResponse.setProductName(product.getName()));
            itemResponses.add(itemResponse);
        }
        response.setItems(itemResponses);
        return response;
    }
}
