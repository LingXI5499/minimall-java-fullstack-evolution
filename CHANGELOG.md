# Changelog

## v4

### Added
- `orders`、`order_item`，订单创建、列表和详情，多 Mapper 事务与回滚演示开关。

### Changed
- 前端加入 Vue Router，拆为商品与订单页面。

### Fixed
- 订单任一步失败时回滚订单、明细和库存更新。

### Learning Focus
- Service 编排、`@Transactional`、提交与回滚。

## v3

### Added
- 统一 `Result<T>`、Bean Validation、业务异常与全局处理、SLF4J 业务日志。

### Changed
- 前端 API 请求拆到 `src/api`，商品页面移至 `src/views`。

### Fixed
- 不存在的商品返回 404，输入错误返回 400，前端显示后端错误消息。

### Learning Focus
- HTTP 状态语义、DTO 校验、异常边界、日志层级与 API 契约。

## v2

### Added
- `ProductQueryDTO`、`PageResult`、MyBatis 动态 SQL、分页和组合查询、每页条数选择器。

### Changed
- 商品列表由全量返回变为分页结果。

### Fixed
- 切换每页条数时回到第一页并重新查询。

### Learning Focus
- `COUNT(*)`、`LIKE`、`LIMIT`、`OFFSET`、`<where>`、`<if>`。

## v1

### Added
- 商品 CRUD、Service、Mapper 接口与 XML、MySQL 建表和种子数据。

### Changed
- Vue 页面由 Echo 进化为商品管理。

### Fixed
- 数据库连接改为环境变量，避免压缩包中的本地配置进入 Git。

### Learning Focus
- DTO、Entity、Mapper Proxy、JDBC、HikariCP、MySQL。

## v0

### Added
- 最小 Spring Boot 请求链、Vue 表单与 Vite 代理。

### Changed
- 建立干净的单仓目录。

### Fixed
- 无。

### Learning Focus
- HTTP、Spring MVC、DTO、Jackson、JSON。
