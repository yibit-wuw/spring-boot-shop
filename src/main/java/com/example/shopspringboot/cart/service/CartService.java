package com.example.shopspringboot.cart.service;

import com.example.shopspringboot.cart.dto.CartResponse;
import com.example.shopspringboot.cart.entity.CartItemEntity;
import com.example.shopspringboot.cart.repository.CartRepository;
import com.example.shopspringboot.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 告訴 Spring Boot：這是一個業務邏輯層（Service）的元件，請將它註冊到 Spring 容器中管理
@Service
public class CartService {
    // 宣告購物車的 repository 變數，加上 final 代表此依賴在初始化後就不會再被修改
    private final CartRepository cartRepository;
    // 宣告商品的 repository 變數，用來跨模組查詢商品狀態
    private final ProductRepository productRepository;
    // 透過建構子（Constructor）進行相依性注入，Spring 會自動把實作好的兩個 repository 物件傳進來
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository; // 將傳進來的購物車數據庫操作工具存入變數
        this.productRepository = productRepository; // 將傳進來的商品數據庫操作工具存入變數
    }
    // 定義一個公開方法：執行「加入購物車」的商務邏輯，需要傳入使用者 ID、商品 ID 與要購買的數量
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 1. 【檢查商品是否存在】
        // 使用商品 repository 去資料庫查詢該商品 ID
        productRepository.findById(productId)
                // 如果找不到該商品（盒子是空的），就立刻丟出異常，中斷後續的所有程式執行
                .orElseThrow(() -> new RuntimeException("沒有此商品"));
        // 2. 【檢查購物車是否已有舊紀錄】
        // 呼叫自訂的魔法方法，同時用 userId 和 productId 去 cart_items 資料表撈撈看
        // 結果會被包在 Optional 盒子裡，用來避免 NullPointerException（空指標異常）
        Optional<CartItemEntity> cartItemOpt = cartRepository.findByUserIdAndProductId(userId, productId);
        // 3. 【分流判斷】檢查 Optional 盒子裡面到底有沒有東西（舊紀錄存在與否）
        if (cartItemOpt.isPresent()) {
            // 【情況 A：更新數量（購物車已有該商品）】
            // 從 Optional 盒子中把這筆已經存在的購物車實體（Entity）拿出來
            CartItemEntity existingItem = cartItemOpt.get();
            // 將「原本舊的數量」加上「這次要新加入的數量」，並重新設定進去
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            // 呼叫 save 方法。因為這筆資料帶有主鍵 id，Spring Data JPA 會自動執行 SQL 的 UPDATE 指令
            cartRepository.save(existingItem);
        } else {
            // 【情況 B：新增一筆（全新加入的商品）】
            // 實例化（new）一個全新、乾淨的購物車實體物件
            CartItemEntity newItem = new CartItemEntity();
            // 設定這個格子的持有人為當前登入的使用者 ID
            newItem.setUserId(userId);
            // 設定這個格子裝的是哪一個商品 ID
            newItem.setProductId(productId);
            // 設定這個商品的初始購買數量
            newItem.setQuantity(quantity);
            // 呼叫 save 方法。因為這筆資料沒有主鍵 id（值為 null），Spring Data JPA 會自動執行 SQL 的 INSERT 指令
            cartRepository.save(newItem);
        }
    }
    public List<CartResponse> getCartByUserId(Long userId) {
        // 1. 去資料庫撈出該使用者的所有購物車項目
        List<CartItemEntity> entities = cartRepository.findByUserId(userId);
        // 2. 準備一個空列表，用來裝等等要還給前端的 DTO 外送盒
        List<CartResponse> responseList = new ArrayList<>();
        // 3. 用 for 迴圈把每一筆 Entity 的資料拿出來，塞進 DTO 裡
        for (CartItemEntity entity : entities) {
            CartResponse dto = new CartResponse();
            dto.setUserId(entity.getUserId());
            dto.setProductId(entity.getProductId());
            dto.setQuantity(entity.getQuantity());
            // 塞完後，把這個外送盒放進準備好的清單裡
            responseList.add(dto);
        }
        // 最後把整袋外送盒清單回傳
        return responseList;
    }
    // 將商品移出購物車
    public void removeFromCart(Long userId, Long productId) {
        // 直接呼叫 repository 的刪除方法
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }

}