package com.example.shopspringboot.exception;

import com.example.shopspringboot.dto.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 讓它繼承 RuntimeException，这样它就具備了隨時可以 throw 的能力
public class BusinessException extends RuntimeException {
    // 建構子：傳入錯誤訊息
    public BusinessException(String message) {
        super(message);
    }
    // 3. 專門捕捉我們自訂的電商業務錯誤
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        return ApiResponse.error(ex.getMessage());
    }
}