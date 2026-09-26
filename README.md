# MiniMall Java Full-Stack Evolution

这是一个通过 Git tags 学习 Java 全栈系统演进的教程项目。每个 tag 是可独立运行的阶段快照。

| Version | Topic | Checkout |
|---|---|---|
| v0 | Spring Boot 请求链 | `git checkout v0` |

当前阶段：v0。后续阶段见 [路线图](docs/roadmap.md)。

## 快速运行

要求 JDK 21、Maven、Node.js。分别在 `minimall-server` 执行 `mvn spring-boot:run`，在 `minimall-web` 执行 `npm ci && npm run dev`。访问 http://localhost:5173。Vite 会将 `/api` 代理到后端 8080。

## 验证

`mvn test && mvn package`；`npm ci && npm run build`。还可请求 `GET /api/hello`、`GET /api/info`、`GET /api/greet?name=Li`、`GET /api/users/1`、`POST /api/echo`。
