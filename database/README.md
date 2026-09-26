# 数据库初始化

要求 MySQL 8。V5 起按顺序执行 `schema.sql`、`migrations/v5.sql`、`seed.sql`。已运行过 V4 的数据库只执行一次 `migrations/v5.sql`。`seed.sql` 仅供首次演示，重复执行会插入重复商品。

后端通过 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 读取连接参数；不要将真实密码提交到仓库。
