# 请求链：一次请求经过哪些位置

```text
浏览器 Vue → Axios → 开发时 Vite / 生产时 Nginx → Spring Security FilterChain
  → DispatcherServlet → Controller → DTO / Validation → Service / Transaction
  → Mapper 接口 → MyBatis XML → JDBC / HikariCP → MySQL
                                       ↘ Result / 异常处理 → JSON → 浏览器
```

- **FilterChain** 在 MVC 之前验证 Bearer JWT 和角色。未登录返回 401，已登录但权限不足返回 403。登录接口无需 JWT，商品写操作需要 ADMIN，订单查询还要在 Service 检查归属。
- **Controller** 接收 HTTP、触发 DTO 校验并返回统一 `Result`；不在这里写 SQL。`GlobalExceptionHandler` 把参数和业务异常转成可读响应。
- **Service** 编排订单、明细和库存的多表操作。`@Transactional` 保证其中一步失败时一起回滚。商品库存通过 SQL 条件更新避免并发超卖。
- **Mapper** 是 Java 接口；MyBatis 用 XML 中的 SQL 实现它，最后经连接池访问 MySQL。
- **Redis** 位于商品详情 Service 的读取旁路：先查缓存，未命中再查 MySQL，写入后使缓存失效；缓存不可用时回退数据库。
- **AOP** 包围商品管理操作记录业务日志。它不代替鉴权，也不代替事务。
- **Actuator** 提供 `/actuator/health` 和指标。生产部署从服务器本机检查健康状态。

WebSocket 有另一条入口：浏览器先用 JWT 调用 `/api/auth/ws-ticket`，再用 60 秒、只能使用一次的票据升级 `/ws/orders`。这个握手由 `WsTicketInterceptor` 验证；连接建立后 `OrderWebSocketHandler` 推送订单事件。定时任务则由时钟触发 `@Scheduled`，不经过 Controller 或 HTTP FilterChain。
