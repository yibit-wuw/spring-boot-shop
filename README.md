# spring-boot-shop
spring-boot java後端商城開發

# 🛒 My Spring Boot Mall (線上商城後端 API 系統)

這是一個基於 **Spring Boot 3.x / 4.x** 與 **Java 21** 開發的現代化線上商城後端 API 系統。專案採用中大型企業主流的**業務模組化架構**進行設計，具備高擴充性與安全性。

## 🛠️ 技術棧 (Tech Stack)
* **核心框架**：Spring Boot, Spring Web (RESTful API)
* **安全防護**：Spring Security (BCrypt 密碼安全加密)
* **身分驗證**：Java JWT (JSON Web Token 無狀態驗證)
* **資料庫操作**：Spring Data JPA (Hibernate)
* **高效快取**：Spring Data Redis (預留優化接口)
* **開發工具**：Lombok, Gradle, MySQL Workbench

## 📂 專案架構 (Project Architecture)
專案全面採用「業務模組化（Feature-by-Package）」流派重構，程式碼層次分明：
* `config/` - 全域安全與環境設定 (安檢大閘門)
* `product/` - 商品功能模組 (查詢展示、庫存管理)
* `user/` - 會員安全模組 (註冊加密、登入發放 JWT)

## 🚀 目前已打通的 API 接口
1. **商品模組**：
    * `GET /api/products` - 獲取商城商品列表（免登入白名單）
2. **會員模組**：
    * `POST /api/auth/register` - 會員註冊（自動啟用 BCrypt 哈希不可逆加密）
    * `POST /api/auth/login` - 會員登入驗證（成功核發 24 小時防偽 JWT 通行證）

## 💻 本機開發運行指南
1. 克隆專案：`git clone <你的倉庫網址>`
2. 修改 `src/main/resources/application.properties` 中的 MySQL 帳號與密碼。
3. 點擊 `ShopdemoApplication.java` 的 Main 方法啟動專案，JPA 將會全自動在您的 MySQL 中建立 `product` 與 `user_table` 資料表。
