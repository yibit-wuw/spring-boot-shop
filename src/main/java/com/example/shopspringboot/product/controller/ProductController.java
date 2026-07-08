package com.example.shopspringboot.product.controller;

import com.example.shopspringboot.dto.ApiResponse;
import com.example.shopspringboot.product.dto.ProductRequest;
import com.example.shopspringboot.product.entity.ProductEntity;
import com.example.shopspringboot.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.findAll();
        // 💡 用 ApiResponse.success 包起來，大功告成！
        return ApiResponse.success("查詢商品列表成功", products);
    }

    // 當前端發送 GET http://localhost:8080/api/products/5 時，會觸發這個方法
    @GetMapping("/{id}")
    public ApiResponse<ProductEntity> getProductById(@PathVariable Long id) {
        // 1. 【呼叫 Service】拿著網址上的 id，精準查出這「一件」商品
        ProductEntity product = productService.findById(id);
        // 2. 【回傳結果】把這單一件商品裝進 ApiResponse 盒子裡還給前端
        return ApiResponse.success("查詢商品成功", product);
    }
    // 當前端發送 POST http://localhost:8080/api/products 時，會觸發商品上架
    @PostMapping
    public ApiResponse<Void> createProduct(@Valid @RequestBody ProductRequest dto) {
        productService.createProduct(dto);
        return ApiResponse.success("商品上架成功");
    }
    // 當前端發送 PUT http://localhost:8080/api/products/5 時，會觸發修改商品
    @PutMapping("/{id}")
    public ApiResponse<Void> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest dto) {

        productService.updateProduct(id, dto);
        return ApiResponse.success("商品修改成功");
    }
    // 當前端發送 DELETE http://localhost:8080/api/products/5 時，會觸發刪除商品
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success("商品刪除成功");
    }
}
