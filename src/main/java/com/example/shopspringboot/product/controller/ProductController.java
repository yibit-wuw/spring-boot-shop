package com.example.shopspringboot.product.controller;

import com.example.shopspringboot.product.entity.Product;
import com.example.shopspringboot.product.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/products") // 這一台控制器的基礎網址路徑
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 當前端發送 GET http://localhost:8080/api/products 時，會觸發這個方法
    @GetMapping
    public List<Product> getProductList() {
        return productService.getAllProducts();
    }
}