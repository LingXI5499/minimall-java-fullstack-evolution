# 本地配置

默认连接 `jdbc:mysql://localhost:3306/minimall`，用户名 `root`，密码为空。若本机不同，请在启动后端的终端设置 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 环境变量。不要将密码写入 Git 追踪文件。

也可以使用被 Git 忽略的 `application-local.properties`，并通过 `--spring.profiles.active=local` 启动。

V6 起 Redis 默认连接 `localhost:6379`，可通过 `REDIS_HOST`、`REDIS_PORT`、`REDIS_PASSWORD` 修改。Redis 暂时不可用时商品详情会退回 MySQL，但无法观察缓存命中。

V7 起 RabbitMQ 默认连接 `localhost:5672`，可通过 `MQ_HOST`、`MQ_PORT`、`MQ_USERNAME`、`MQ_PASSWORD` 修改。RabbitMQ 不可用时订单仍会提交，但异步通知无法送达；生产可靠投递需要 Outbox 等持久化方案。

V8 起 `WS_ALLOWED_ORIGINS` 控制 WebSocket 握手来源，默认允许本地 Vite 和 8080。`app.orders.timeout-seconds` 默认 1800 秒，`app.orders.expiry-scan-ms` 默认 60000 毫秒；本地验证可临时缩短，生产请按业务调整。
