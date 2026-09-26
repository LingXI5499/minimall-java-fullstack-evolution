# Changelog

## v9

### Added
- JWT 登录、Spring Security FilterChain、ADMIN/USER 权限、订单归属校验与 WebSocket 一次性票据。
- 商品操作 AOP 日志、OpenAPI、Actuator、服务/Mapper/事务/Controller/安全测试、GitHub Actions。

### Changed
- 前端加入登录页面和会话处理，普通用户只看到自己的订单。

### Fixed
- 管理员 WebSocket 通知不再对未授权连接开放。

### Learning Focus
- 认证、授权、过滤链、JWT、AOP、测试分层与 CI。

## v8

### Added
- `/ws/orders` 实时订单事件、前端连接状态与通知列表。
- `@Scheduled` 超时关闭待处理订单并恢复库存。

### Changed
- WebSocket 推送与缓存失效都在订单事务提交后执行。

### Fixed
- 超时关闭时不再永久占用待处理订单的库存。

### Learning Focus
- WebSocket 握手/推送、时间驱动入口、定时任务事务边界。

## v7

### Added
- RabbitMQ Exchange、Queue、Binding、死信队列、订单事件生产者与模拟通知消费者。
- `order_notification` 唯一键去重记录。

### Changed
- 订单创建在数据库提交后才发布 MQ 消息。

### Fixed
- 订单回滚时不会提前发送“创建成功”消息。

### Learning Focus
- Producer、Consumer、ACK、Retry、DLQ、幂等和事务边界。

## v6

### Added
- Redis 商品详情 Cache Aside、60 秒 TTL、20 秒空值缓存与命中/未命中/失效日志。

### Changed
- 商品写入后失效缓存；订单事务提交后再失效受影响商品缓存。

### Fixed
- Redis 不可用时回退 MySQL，缓存故障不阻断商品查询。

### Learning Focus
- Cache Aside、缓存穿透、TTL、失效时机与故障降级。

## v5

### Added
- 分类表与接口、订单明细 JOIN 视图、外键和查询索引、EXPLAIN 示例。

### Changed
- 库存扣减变为数据库单条条件 UPDATE，并检查受影响行数。

### Fixed
- 并发下两个请求不能同时扣掉同一件库存。

### Learning Focus
- 关系、索引、行锁、MVCC、原子更新与隔离级别。

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
