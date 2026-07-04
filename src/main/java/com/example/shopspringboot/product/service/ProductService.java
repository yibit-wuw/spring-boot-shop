package com.example.shopspringboot.product.service;

import com.example.shopspringboot.product.entity.Product;
import com.example.shopspringboot.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 透過建構子自動注入 Repository
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 業務邏輯：獲取所有商品列表
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}