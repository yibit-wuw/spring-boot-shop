package com.example.shopspringboot.exception;

import com.example.shopspringboot.dto.ApiResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全域例外處理器
 *
 * 使用 @RestControllerAdvice 攔截 Controller 中拋出的例外，
 * 統一回傳 ApiResponse，讓前端收到一致的 JSON 格式。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 處理業務邏輯錯誤
     * 例如：
     * throw new RuntimeException("商品不存在");
     */
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> handleRuntimeException(RuntimeException ex) {
        return ApiResponse.error(ex.getMessage());
    }
    /**
     * 處理 DTO 驗證失敗 (@Valid)
     * 將所有驗證錯誤合併回傳。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return ApiResponse.error(message);
    }
    /**
     * 處理所有未預期例外
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex) {

        // 開發階段方便除錯
        ex.printStackTrace();
        return ApiResponse.error("系統發生未知錯誤，請稍後再試");
    }
}