# NEXT_SHOP Backend

NEXT_SHOP 商城後端 REST API，使用 Spring Boot + Spring Security + JWT 建置，提供會員、商品、購物車、訂單等完整商城功能。

## 技術棧

| 類別 | 技術 | 版本 |
|------|------|------|
| 語言 | Java | 21 |
| 框架 | Spring Boot | 4.1.0 |
| 建置 | Gradle | — |
| 資料庫 | MySQL | 8.x |
| 快取 | Redis | 可選（未啟動時自動降級） |
| 安全 | Spring Security + JWT | auth0 java-jwt 4.4.0 |
| ORM | Spring Data JPA | — |
| 工具 | Lombok / Validation / DevTools | — |

## 前置需求

- JDK 21
- MySQL（預設 `shop_db`，帳密見下方設定）
- Redis（**可選**，未啟動不影響 API，僅失去快取功能）

## 快速開始

```bash
# 進入後端目錄
cd shop-springboot

# 確認 MySQL 已啟動，並修改 application.properties 連線設定

# 啟動應用（預設 http://localhost:8080）
./gradlew bootRun

# 編譯（不執行）
./gradlew compileJava

# 執行測試
./gradlew test
```

首次啟動時 JPA 會自動建表（`ddl-auto=update`），並由 `AdminInitializer` 建立預設管理員帳號。

## 設定檔

`src/main/resources/application.properties`：

| 設定項 | 預設值 | 說明 |
|--------|--------|------|
| `server.port` | `8080` | 服務埠 |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/shop_db` | MySQL 連線 |
| `spring.datasource.username` | `root` | 資料庫帳號 |
| `spring.datasource.password` | `123456` | 資料庫密碼 |
| `jwt.secret` | （自訂） | JWT 簽章密鑰 |
| `admin.username` | `admin` | 預設管理員帳號 |
| `admin.password` | `admin123` | 預設管理員密碼（BCrypt 加密後存入 DB） |
| `file.upload-dir` | `./uploads` | 商品圖片儲存目錄 |
| `spring.data.redis.host` | `localhost` | Redis 位址（可選） |

## 專案結構

```
src/main/java/com/example/shopspringboot/
├── config/           # SecurityConfig、JwtFilter、Redis、AdminInitializer
├── dto/              # ApiResponse、PageResponse
├── exception/        # BusinessException、GlobalExceptionHandler
├── user/             # 會員（Entity、DTO、Service、Controller）
├── product/          # 商品
├── cart/             # 購物車
├── order/            # 訂單
└── upload/           # 圖片上傳
```

## API 端點

所有回應皆包裝為 `ApiResponse<T>`：

```json
{ "success": true, "message": "...", "data": { ... } }
```

認證方式：`Authorization: Bearer <JWT Token>`

### 公開（不需 Token）

| Method | Path | 說明 |
|--------|------|------|
| POST | `/api/auth/login` | 登入，回傳 token + username + role |
| POST | `/api/auth/register` | 註冊（role 固定為 USER） |
| GET | `/api/products?page=&size=` | 商品列表（分頁，預設 9 筆/頁） |
| GET | `/api/products/{id}` | 商品詳情 |
| GET | `/api/products/search?keyword=&page=&size=` | 商品搜尋 |
| GET | `/uploads/**` | 商品圖片靜態資源 |

### 需登入（USER 或 ADMIN）

| Method | Path | 說明 |
|--------|------|------|
| POST | `/api/cart` | 加入購物車 `{ productId, quantity }` |
| GET | `/api/cart` | 購物車列表 |
| DELETE | `/api/cart/{productId}` | 移除購物車商品 |
| POST | `/api/orders` | 結帳，回傳 `{ orderId }` |
| GET | `/api/orders?page=&size=` | 我的訂單（分頁，預設 5 筆/頁） |
| GET | `/api/orders/{id}` | 訂單詳情 |
| POST | `/api/orders/{id}/pay` | 付款（PENDING → PAID） |
| POST | `/api/orders/{id}/cancel` | 取消訂單（還原庫存） |

### 僅管理員（ROLE_ADMIN）

| Method | Path | 說明 |
|--------|------|------|
| POST | `/api/products` | 商品上架 |
| PUT | `/api/products/{id}` | 商品修改 |
| DELETE | `/api/products/{id}` | 商品刪除 |
| POST | `/api/upload` | 圖片上傳（multipart，`file` 欄位） |

## 安全機制

- **Session**：`STATELESS`（純 JWT，不建立 HttpSession）
- **密碼**：BCrypt 加密
- **JWT**：24 小時有效，payload 含 `username`（subject）與 `role` claim
- **角色**：`USER`（一般會員）、`ADMIN`（管理員）
- **CORS**：允許 `http://localhost:5173`、`http://127.0.0.1:5173`
- **403**：非 ADMIN 呼叫管理 API 回 `{ "success": false, "message": "無管理員權限" }`

## 管理員初始化

啟動時 `AdminInitializer` 會檢查 DB：

- 若 `admin.username` 不存在 → 自動建立（密碼 BCrypt 加密）
- 若已存在但 role 非 ADMIN → 自動提升為 ADMIN

預設帳密：`admin` / `admin123`

## 資料表

JPA 自動管理，主要資料表：

| 資料表 | 說明 |
|--------|------|
| `user_table` | 會員（含 role 欄位） |
| `product` | 商品 |
| `cart_items` | 購物車 |
| `orders` | 訂單主表 |
| `order_items` | 訂單明細 |

## 訂單狀態

| 狀態 | 說明 |
|------|------|
| `PENDING` | 待付款（結帳後預設） |
| `PAID` | 已付款 |
| `CANCELLED` | 已取消（庫存還原） |

## Redis 說明

商品列表使用 Redis 快取（`ProductCacheService`）。若 Redis 未啟動，會自動降級查詢 MySQL，**不影響正常運作**。

## 圖片上傳

- 上傳目錄：`./uploads/`（專案根目錄下）
- 支援格式：jpg、jpeg、png、gif、webp
- 大小限制：5MB
- 回傳 URL 格式：`/uploads/{uuid}.{ext}`
- 讀取：`GET /uploads/**`（公開，不需 Token）

## 測試帳號

| 角色 | 帳號 | 密碼 |
|------|------|------|
| 管理員 | `admin` | `admin123` |
| 一般會員 | 透過 `/api/auth/register` 註冊 | — |

## 與前端配合

1. 先啟動本後端（port 8080）
2. 再啟動前端 `npm run dev`（port 5173）
3. 前端 Vite Proxy 轉發 `/api` 與 `/uploads` 至本服務

詳見：`../frontend/README.md`

## 相關文件

- 前端 README：`../frontend/README.md`
