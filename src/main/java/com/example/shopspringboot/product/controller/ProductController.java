package com.example.shopspringboot.product.controller;

import com.example.shopspringboot.dto.ApiResponse;
import com.example.shopspringboot.dto.PageResponse;
import com.example.shopspringboot.product.dto.ProductRequest;
import com.example.shopspringboot.product.entity.ProductEntity;
import com.example.shopspringboot.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductEntity>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        PageResponse<ProductEntity> products = productService.findAll(page, size);
        return ApiResponse.success("查詢商品列表成功", products);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductEntity> getProductById(@PathVariable Long id) {
        ProductEntity product = productService.findById(id);
        return ApiResponse.success("查詢商品成功", product);
    }

    @PostMapping
    public ApiResponse<Void> createProduct(@Valid @RequestBody ProductRequest dto) {
        productService.createProduct(dto);
        return ApiResponse.success("商品上架成功");
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest dto) {
        productService.updateProduct(id, dto);
        return ApiResponse.success("商品修改成功");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success("商品刪除成功");
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<ProductEntity>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        PageResponse<ProductEntity> results = productService.searchProducts(keyword, page, size);
        return ApiResponse.success("商品搜尋成功", results);
    }
}
