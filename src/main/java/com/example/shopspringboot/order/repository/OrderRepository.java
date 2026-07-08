package com.example.shopspringboot.order.repository;

import com.example.shopspringboot.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // 繼承 JpaRepository 就自動擁有 save()、findById()
}