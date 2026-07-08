package com.example.shopspringboot.product.repository;

import com.example.shopspringboot.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // 繼承後，自動擁有全套的 CRUD（增刪改查）功能！
}