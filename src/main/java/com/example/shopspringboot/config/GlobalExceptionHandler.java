package com.example.shopspringboot.config;

import com.example.shopspringboot.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;
/**
 * 全域例外處理器
 * 使用 @RestControllerAdvice 攔截整個專案中的例外，
 * 統一回傳 API 格式，避免每個 Controller 都要重複撰寫錯誤處理。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 處理 @Valid 驗證失敗所拋出的例外
     * 當 Controller 接收到的 RequestBody 不符合 DTO 上的驗證規則
     * （例如 @NotBlank、@Email、@Size 等）時，
     * Spring 會自動拋出 MethodArgumentNotValidException，
     * 並由此方法統一處理。
     * @param ex 驗證失敗的例外物件
     * @return 回傳 HTTP 400 (Bad Request) 與自訂 ApiResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        // 取得所有欄位的錯誤訊息，並以全形分號（；）串接
        String message = ex.getBindingResult().getFieldErrors().stream()
                // 取出每個欄位設定的錯誤訊息
                .map(FieldError::getDefaultMessage)
                // 將多個錯誤訊息合併成一個字串
                .collect(Collectors.joining("；"));
        // 回傳 HTTP 400，並使用 ApiResponse.error() 封裝錯誤訊息
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message));
    }
}
