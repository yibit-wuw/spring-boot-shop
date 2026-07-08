package com.example.shopspringboot.exception;

import com.example.shopspringboot.dto.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 1. 專門捕捉業務邏輯錯誤 (如：庫存不足、購物車空空的)
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> handleRuntimeException(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        return ApiResponse.error(errorMessage);
    }
    // 2. 專門捕捉 DTO 參數驗證失敗 (如：商品數量少於 1、ID 為空)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException ex) {
        // 從錯誤對象中精準抽取出你寫在 DTO 上的 message 內容
        String defaultMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.error(defaultMessage);
    }
}