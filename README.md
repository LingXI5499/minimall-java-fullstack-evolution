# MiniMall Java Full-Stack Evolution

这是一个通过 Git tags 学习 Java 全栈系统演进的教程项目。每个 tag 是可独立运行的阶段快照。

| Version | Topic | Checkout |
|---|---|---|
| v0 | Spring Boot 请求链 | `git checkout v0` |
| v1 | 商品 CRUD 与 MyBatis | `git checkout v1` |

当前阶段：v1。后续阶段见 [路线图](docs/roadmap.md)。

## 快速运行

要求 JDK 21、Maven、Node.js、MySQL 8。先执行 `database/schema.sql` 和 `database/seed.sql`，按 [配置说明](docs/configuration.md) 设置数据库环境变量。分别在 `minimall-server` 执行 `mvn spring-boot:run`，在 `minimall-web` 执行 `npm ci && npm run dev`。访问 http://localhost:5173。Vite 会将 `/api` 代理到后端 8080。

## 验证

`mvn test && mvn package`；`npm ci && npm run build`。页面可完成商品新增、列表、按 ID 查询、修改和删除。V0 Echo 接口仍可用于回顾请求链。
