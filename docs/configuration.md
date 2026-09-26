# 本地配置

默认连接 `jdbc:mysql://localhost:3306/minimall`，用户名 `root`，密码为空。若本机不同，请在启动后端的终端设置 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 环境变量。不要将密码写入 Git 追踪文件。

也可以使用被 Git 忽略的 `application-local.properties`，并通过 `--spring.profiles.active=local` 启动。

V6 起 Redis 默认连接 `localhost:6379`，可通过 `REDIS_HOST`、`REDIS_PORT`、`REDIS_PASSWORD` 修改。Redis 暂时不可用时商品详情会退回 MySQL，但无法观察缓存命中。

V7 起 RabbitMQ 默认连接 `localhost:5672`，可通过 `MQ_HOST`、`MQ_PORT`、`MQ_USERNAME`、`MQ_PASSWORD` 修改。RabbitMQ 不可用时订单仍会提交，但异步通知无法送达；生产可靠投递需要 Outbox 等持久化方案。

V8 起 `WS_ALLOWED_ORIGINS` 控制 WebSocket 握手来源，默认允许本地 Vite 和 8080。`app.orders.timeout-seconds` 默认 1800 秒，`app.orders.expiry-scan-ms` 默认 60000 毫秒；本地验证可临时缩短，生产请按业务调整。

V9 起必须设置 `JWT_SECRET`（至少 32 个 UTF-8 字节）、`ADMIN_PASSWORD` 和 `USER_PASSWORD`（各至少 12 位）。可选 `ADMIN_USERNAME`、`USER_USERNAME`，默认分别是 `admin`、`user`。未配置时服务会拒绝启动；请在本机或部署平台用环境变量注入，不要写入 Git。JWT 有效期 2 小时。浏览器将 token 放在 sessionStorage，关闭浏览器会话即清除；不要在页面插入不可信脚本。

接口文档：`/swagger-ui.html` 与 `/v3/api-docs`。健康检查：`/actuator/health`；指标：`/actuator/metrics`（需 ADMIN）。集成测试需要 MySQL 预先初始化并设置 `RUN_DB_TESTS=true`，数据库连接仍使用 `DB_*` 环境变量。

V10 生产环境使用 `--spring.profiles.active=prod`：后端只监听 `127.0.0.1:8080`，OpenAPI 调试页面关闭，Nginx 提供 HTTPS、前端静态文件及 `/api`、`/ws` 代理。复制 [环境变量模板](../deploy/minimall.env.example) 到服务器 `/etc/minimall/minimall.env`，替换全部示例密码并设为 600 权限；`WS_ALLOWED_ORIGINS` 要与实际 HTTPS 域名一致。完整步骤见 [部署手册](architecture/deployment.md)。
