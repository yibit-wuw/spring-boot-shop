package com.example.shopspringboot.product.service;

import com.example.shopspringboot.config.ProductCacheService;
import com.example.shopspringboot.dto.PageResponse;
import com.example.shopspringboot.exception.BusinessException;
import com.example.shopspringboot.product.dto.ProductRequest;
import com.example.shopspringboot.product.entity.ProductEntity;
import com.example.shopspringboot.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    private static final String ALL_PRODUCTS_KEY = "products::all";

    private final ProductRepository productRepository;
    private final ProductCacheService cacheService;

    public ProductService(ProductRepository productRepository, ProductCacheService cacheService) {
        this.productRepository = productRepository;
        this.cacheService = cacheService;
    }

    public PageResponse<ProductEntity> findAll(int page, int size) {
        Page<ProductEntity> result = productRepository.findAll(PageRequest.of(page, size));
        return toPageResponse(result);
    }

    public ProductEntity findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("該商品已下架或不存在"));
    }

    public void createProduct(ProductRequest dto) {
        ProductEntity product = new ProductEntity();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());

        productRepository.save(product);
        cacheService.delete(ALL_PRODUCTS_KEY);
    }

    public void updateProduct(Long id, ProductRequest dto) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("找不到該商品，無法修改"));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());

        productRepository.save(product);
        cacheService.delete(ALL_PRODUCTS_KEY);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException("找不到該商品，無法刪除");
        }
        productRepository.deleteById(id);
        cacheService.delete(ALL_PRODUCTS_KEY);
    }

    public PageResponse<ProductEntity> searchProducts(String keyword, int page, int size) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll(page, size);
        }
        Page<ProductEntity> result = productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword.trim(), keyword.trim(), PageRequest.of(page, size));
        return toPageResponse(result);
    }

    private PageResponse<ProductEntity> toPageResponse(Page<ProductEntity> page) {
        return new PageResponse<>(
                new ArrayList<>(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
