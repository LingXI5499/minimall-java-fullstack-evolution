# 数据库初始化

要求 MySQL 8。V5 起按顺序执行 `schema.sql`、`migrations/v5.sql`、`seed.sql`。已运行过 V4 的数据库只执行一次 `migrations/v5.sql`。`seed.sql` 仅供首次演示，重复执行会插入重复商品。

V7 起再执行 `migrations/v7.sql`，给 RabbitMQ 消费者准备通知去重表。

V9 起再执行 `migrations/v9.sql`，增加订单归属用户字段。之前的订单 owner 为空，只对管理员可见。

后端通过 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 读取连接参数；不要将真实密码提交到仓库。
