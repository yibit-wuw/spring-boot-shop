// 指定此類別所在的 package（套件）
package com.example.shopspringboot.dto;
// 匯入 Lombok 的 @AllArgsConstructor
// 自動產生包含所有屬性的建構子
import lombok.AllArgsConstructor;
// 匯入 Lombok 的 @Data
// 自動產生 Getter、Setter、toString()、equals()、hashCode()
import lombok.Data;
// 匯入 Lombok 的 @NoArgsConstructor
// 自動產生無參數建構子
import lombok.NoArgsConstructor;
// @Data
// 自動產生：
// Getter、Setter、toString()、equals()、hashCode()
@Data
// 自動產生無參數建構子
@NoArgsConstructor
// 自動產生包含所有欄位的建構子
@AllArgsConstructor
// 宣告一個泛型(Generic)類別
// <T> 代表 data 可以存放任何型別
// 例如：String、User、LoginResponse、List<ProductEntity>...
public class ApiResponse<T> {
    // API 是否成功
    // true = 成功
    // false = 失敗
    private boolean success;
    // 回傳給前端的訊息
    // 例如：「登入成功」、「註冊成功」、「帳號不存在」
    private String message;
    // 真正回傳的資料
    // T 是泛型，因此可以放任何物件
    private T data;
    // ===========================
    // 成功回傳（有資料）
    // ===========================
    // static 表示不用建立物件即可呼叫
    // ApiResponse.success(...)
    public static <T> ApiResponse<T> success(String message, T data) {
        // 建立新的 ApiResponse
        // success=true
        // message=傳入的訊息
        // data=回傳資料
        return new ApiResponse<>(true, message, data);
    }
    // ===========================
    // 成功回傳（沒有資料）
    // ===========================
    public static <T> ApiResponse<T> success(String message) {
        // data 設為 null
        // 常用於新增、刪除、修改成功
        return new ApiResponse<>(true, message, null);
    }
    // ===========================
    // 失敗回傳
    // ===========================
    public static <T> ApiResponse<T> error(String message) {
        // success=false
        // data=null
        // 回傳錯誤訊息
        return new ApiResponse<>(false, message, null);
    }
}