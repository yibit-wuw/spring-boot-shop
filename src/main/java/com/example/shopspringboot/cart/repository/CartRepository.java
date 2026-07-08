package com.example.shopspringboot.cart.repository;

import com.example.shopspringboot.cart.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository  extends JpaRepository<CartItemEntity, Long> {
    // 繼承後，自動擁有全套的 CRUD（增刪改查）功能！
    // 意思：透過 UserId 和 ProductId 尋找購物車項目
    Optional<CartItemEntity> findByUserIdAndProductId(Long userId, Long productId);
    // 意思：去 cart_items 表裡，找出所有 user_id 等於傳進來這個數字的資料，並塞進一個 List（列表）裡
    List<CartItemEntity> findByUserId(Long userId);

    // 意思：根據 userId 和 productId，把對應的那一筆購物車紀錄直接從資料庫刪除
    // 加上 @Transactional 是因為刪除屬於變動性操作，需要事務支援
    @org.springframework.transaction.annotation.Transactional
    void deleteByUserIdAndProductId(Long userId, Long productId);
}
