# Changelog

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
