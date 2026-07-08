package com.example.shopspringboot.order.repository;

import com.example.shopspringboot.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    // 明細表的資料庫操作工具
}