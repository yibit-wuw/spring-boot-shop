package com.example.shopspringboot.product.service;

import com.example.shopspringboot.exception.BusinessException;
import com.example.shopspringboot.product.dto.ProductRequest; // 💡 引入我們剛做好的 DTO
import com.example.shopspringboot.product.entity.ProductEntity;
import com.example.shopspringboot.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    // 透過建構子自動注入 repository
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // 1. 業務邏輯：獲取所有商品列表
    public List<ProductEntity> findAll() {
        return productRepository.findAll();
    }
    // 2. 根據商品 ID 查詢單一商品
    public ProductEntity findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("該商品已下架或不存在"));
    }
    // 💡 3. 商品上架（新增商品）
    public void createProduct(ProductRequest dto) {
        // 建立一個全新的商品 Entity 空殼
        ProductEntity product = new ProductEntity();
        // 從外送盒 (DTO) 裡面拿出資料，塞進 Entity 空殼中
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        // 💡 可以在這裡設定初始狀態，例如上架中（如果你的 Entity 有 status 欄位的話，沒有就不用寫）
        // product.setStatus("ACTIVE");
        // 呼叫 save 方法。因為這筆資料沒有主鍵 id，JPA 會自動執行 INSERT 將商品存入資料庫
        productRepository.save(product);
    }
    // 💡 4. 修改商品資料
    public void updateProduct(Long id, ProductRequest dto) {
        // 先查出這件商品「原本在資料庫的樣子」，找不到就丟出我們自訂的 BusinessException
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("找不到該商品，無法修改"));
        // 把前端傳來的新資料，覆蓋掉舊資料
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        // 呼叫 save 方法。因為這筆 product 已經有 id 了，JPA 會自動執行 UPDATE 動作
        productRepository.save(product);
    }
    // 💡 5. 刪除/下架商品
    public void deleteProduct(Long id) {
        // 檢查這個商品到底存不存在
        if (!productRepository.existsById(id)) {
            throw new BusinessException("找不到該商品，無法刪除");
        }
        // 確定存在，直接從資料庫消滅它
        productRepository.deleteById(id);
    }
}