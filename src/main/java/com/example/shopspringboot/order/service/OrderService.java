package com.example.shopspringboot.order.service;

import com.example.shopspringboot.cart.entity.CartItemEntity;
import com.example.shopspringboot.cart.repository.CartRepository;
import com.example.shopspringboot.exception.BusinessException;
import com.example.shopspringboot.order.entity.OrderEntity;
import com.example.shopspringboot.order.entity.OrderItemEntity;
import com.example.shopspringboot.order.repository.OrderItemRepository;
import com.example.shopspringboot.order.repository.OrderRepository;
import com.example.shopspringboot.product.entity.ProductEntity;
import com.example.shopspringboot.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
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
    // 建立訂單（結帳）的業務邏輯
    @Transactional
    public void createOrder(Long userId) {
        // 【第一道工序】：收銀員拉過客人的購物車，看看裡面有什麼
        List<CartItemEntity> cartItems = cartRepository.findByUserId(userId);
        // 【第二道工序】：防呆，如果購物車是空的，直接把客人趕走，不給結帳
        if (cartItems.isEmpty()) {
            throw new BusinessException("購物車是空的，無法結帳！");
        }
        // 準備一個收銀機計算機，用來累加這筆訂單的總金額（初始值是 0 元）
        BigDecimal totalPrice = BigDecimal.ZERO;
        // 【第三道工序】：用 for 迴圈，把購物車裡的商品「一件一件拿出來刷條碼」
        for (CartItemEntity item : cartItems) {
            // 1. 刷條碼：查出商品完整資訊
            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("此商品已下架或不存在"));
            // 2. 檢查庫存：如果「商品庫存」小於「客人想買的數量」
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("商品 " + product.getName() + " 庫存不足，無法結帳！");
            }
            // 3. 扣除庫存：把商品原本的庫存，減掉客人買的數量，並更新回去
            int remainingStock = product.getStock() - item.getQuantity();
            product.setStock(remainingStock);
            productRepository.save(product); // 儲存回商品表，庫存正式減少！
            // 4. 計算金額：商品單價 (BigDecimal) 乘以 客人買的數量 (Integer)
            // 註：BigDecimal 的乘法要用 .multiply()，數量要轉換成 BigDecimal.valueOf()
            BigDecimal itemTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            // 5. 累加到總金額：計算機把這件商品的總價加進 totalPrice 裡
            totalPrice = totalPrice.add(itemTotalPrice);
        }
        // 1. 建立一個全新的訂單主表物件
        OrderEntity order = new OrderEntity();
        // 2. 塞入資料
        order.setUserId(userId);
        order.setTotalPrice(totalPrice); // 剛剛迴圈算好的總金額
        order.setStatus("PENDING");      // 設定初始狀態為 PENDING (待付款)
        order.setCreatedAt(java.time.LocalDateTime.now()); // 設定訂單建立時間為現在
        // 3. 存進資料庫！
        // 💡 重點：save 完之後，要把存好的訂單接回來（savedOrder），
        // 這樣我們才能拿到資料庫自動遞增生成的 orderId（訂單編號），等一下明細表才用得到！
        OrderEntity savedOrder = orderRepository.save(order);


        // 用迴圈把購物車的東西一件一件轉成訂單明細
        for (CartItemEntity item : cartItems) {
            // 1. 建立一個全新的訂單明細物件
            OrderItemEntity orderItem = new OrderItemEntity();
            // 2. 塞入關聯資料
            orderItem.setOrderId(savedOrder.getId()); // 綁定剛剛建立成功的訂單主表 ID！
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            // 💡 這裡有個安全小細節：明細表要記錄當下的價格
            // 我們再去查一次商品價格，確保金額百分之百正確
            ProductEntity product = productRepository.findById(item.getProductId()).get();
            orderItem.setPrice(product.getPrice());
            // 3. 存進訂單明細表
            orderItemRepository.save(orderItem);
        }
        // 【最後的大掃除】：客人都買單了，把他的購物車清空！
        // 呼叫我們之前在購物車寫好的 deleteAll(cartItems) 或是依照你 Repository 的規格刪除
        cartRepository.deleteAll(cartItems);
    }
}