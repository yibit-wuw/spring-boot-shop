package com.example.shopspringboot.product.repository;

import com.example.shopspringboot.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 繼承後，自動擁有全套的 CRUD（增刪改查）功能！
}