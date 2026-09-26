# MiniMall Java Full-Stack Evolution

这是一个“通过 Git tags 学习 Java 全栈系统演进”的教程项目。每个 tag 都是一份独立的阶段快照，可以从最小请求链逐步走到可部署的单体应用。

| Version | Topic | Checkout |
|---|---|---|
| v0 | Spring Boot 请求链 | `git checkout v0` |
| v1 | 商品 CRUD 与 MyBatis | `git checkout v1` |
| v2 | 分页、模糊查询与动态 SQL | `git checkout v2` |
| v3 | 统一响应、校验、异常与日志 | `git checkout v3` |
| v4 | 订单与事务 | `git checkout v4` |
| v5 | 数据关系、索引与库存并发 | `git checkout v5` |
| v6 | Redis 缓存 | `git checkout v6` |
| v7 | RabbitMQ 异步消息 | `git checkout v7` |
| v8 | WebSocket 与定时任务 | `git checkout v8` |
| v9 | Security、测试、文档、监控与 CI | `git checkout v9` |
| v10 | Linux、Nginx 与 HTTPS 部署 | `git checkout v10` |

## 项目目标与学习路径

从浏览器请求如何到达 Controller 开始，逐阶段加入数据库 CRUD、查询分页、统一响应、事务、并发库存、缓存、消息、实时推送、鉴权、测试和部署。建议按 tag 顺序学习，并阅读对应的 [阶段文档](docs/roadmap.md)；不要直接拿最终版本推断早期代码为何这样写。查看历史可用 `git log --oneline --decorate`，切换阶段用 `git checkout vN`，返回最新版本用 `git switch main`。tag 检出时处于 detached HEAD；若要修改某阶段，先创建自己的分支。

## 技术栈与最终架构

前端为 Vue 3、Vite、Vue Router、Axios；后端为 Java 21、Spring Boot、Spring MVC、Spring Security、MyBatis、Maven；数据和消息使用 MySQL 8、Redis、RabbitMQ。JUnit、OpenAPI、Actuator、GitHub Actions 覆盖测试和工程检查。

```text
浏览器 Vue → 开发时 Vite / 生产时 HTTPS Nginx → Spring Security → MVC
  → Controller → Service + Transaction → Mapper / MyBatis → MySQL
                      ├→ Redis 商品缓存
                      ├→ RabbitMQ 订单事件
                      └→ WebSocket 订单通知
时钟 → @Scheduled → 订单到期关闭
```

详细位置见 [请求链](docs/architecture/request-chain.md)、[数据流](docs/architecture/data-flow.md)和[部署架构](docs/architecture/deployment.md)。

## 目录结构

| 目录 | 内容 |
|---|---|
| `minimall-server/` | Spring Boot API、业务逻辑、Mapper、测试 |
| `minimall-web/` | Vue 页面和 API 调用 |
| `database/` | 基础表、阶段迁移与演示数据 |
| `docs/stages/` | V0–V10 阶段说明 |
| `docs/architecture/` | 请求链、数据流和部署说明 |
| `deploy/` | Nginx、systemd、环境变量示例 |
| `.github/workflows/` | 后端与前端 CI |

## 快速运行

准备 JDK 21、Maven、Node.js、MySQL 8、Redis、RabbitMQ。新数据库按 [数据库初始化说明](database/README.md) 执行 `schema.sql`、V5/V7/V9 迁移，演示数据最后只导入一次。按 [环境变量说明](docs/configuration.md) 设置 `DB_*`、`REDIS_*`、`MQ_*`、`ADMIN_PASSWORD`、`USER_PASSWORD`、`JWT_SECRET`，真实密码不要写入仓库。

两个终端分别运行：

```bash
cd minimall-server
mvn spring-boot:run
```

```bash
cd minimall-web
npm ci
npm run dev
```

浏览器打开 `http://localhost:5173` 登录。Vite 将 `/api` 和 `/ws` 代理到后端 8080。管理员可维护商品和看全部订单，普通用户可看商品并创建、查看自己的订单。运行 `mvn test && mvn package`、`npm run build` 检查构建；设置 `RUN_DB_TESTS=true` 并准备好数据库后，后端还会运行集成测试。CI 使用 MySQL 和 Redis 服务执行这些测试。

## 生产部署

构建前端 dist 和后端 jar，使用 [Linux 部署步骤](docs/architecture/deployment.md)配置 Nginx、HTTPS、systemd 与秘密环境文件。应用的 prod profile 只在 `127.0.0.1:8080` 监听；公网由 Nginx 提供静态文件、`/api` 和 `/ws`。本仓库提供部署材料，实际域名、证书、服务器和依赖账号须在目标主机配置。
