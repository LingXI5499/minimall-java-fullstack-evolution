# MiniMall Java 全栈阶段式进化项目开发规范

> 文档用途：交给 Codex / Agent 作为项目重构、阶段化开发、Git 版本管理与 GitHub 发布的唯一执行规范。  
> 项目定位：不是“做一个功能最多的商城”，而是用一个可运行的小型 MiniMall，把 Java 全栈请求链从最基础的 Spring Boot 启动，一步一步进化到可部署的企业级教学项目。  
> 教学核心：**纵向请求链 + 横向工程能力 + Git 可回溯版本**。  
> 版本范围：`v0` → `v10`。  
> 每一个 tag 都必须是一个真实、独立、可运行、可复现的阶段快照。

---

## 1. 项目名称与 GitHub 仓库

### 1.1 推荐 GitHub 仓库名称

```text
minimall-java-fullstack-evolution
```

### 1.2 仓库描述

```text
A staged Java full-stack learning project that evolves from a minimal Spring Boot request chain to CRUD, transactions, Redis, MQ, WebSocket, security and production deployment, preserved through Git tags v0-v10.
```

### 1.3 仓库定位

该仓库不是普通“最终版源码仓库”，而是：

```text
一个项目
+
11 个阶段快照
+
11 套可 checkout 的教学版本
+
完整教程文档
+
真实 Git 演进历史
```

用户应能够执行：

```bash
git checkout v0
git checkout v1
git checkout v2
...
git checkout v10
```

看到项目从最简单状态逐步演进。

---

# 2. 当前素材审计结论

本开发规范基于以下两个阶段性压缩包整理：

```text
基础CRUDminimall.zip
条件分页模糊查询minimall.zip
```

## 2.1 `基础CRUDminimall.zip` 当前能力

已存在：

```text
minimall-server
minimall-web
```

后端已包含：

```text
Spring Boot 4.1.1
JDK 21
Spring MVC
MyBatis Spring Boot Starter 4.0.0
MySQL Connector/J
HikariCP
Controller
DTO
Entity
Service / ServiceImpl
Mapper Interface
Mapper XML
MySQL CRUD
```

商品模块已经存在：

```text
POST   /api/products
GET    /api/products/{id}
GET    /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

前端已经存在：

```text
Vue 3
Vite
Axios
Vite /api Proxy
商品新增
商品查询
商品编辑
商品删除
根据 ID 查询
商品列表
```

因此：

> `基础CRUDminimall.zip` 应作为 `v1` 的主要代码依据。

---

## 2.2 `条件分页模糊查询minimall.zip` 当前能力

相较基础 CRUD 版本，增加：

```text
ProductQueryDTO
PageResult<T>

countByCondition()
selectPageByCondition()

MyBatis <where>
MyBatis <if>

name LIKE 模糊查询
status 条件查询
minPrice / maxPrice 范围查询

page
pageSize
offset

前端筛选条件
上一页
下一页
总记录数
总页数
```

因此：

> `条件分页模糊查询minimall.zip` 应作为 `v2` 的主要代码依据。

### 当前仍需补齐

当前压缩包虽然已经存在：

```javascript
const pageSize = ref(5)
```

并且会把：

```text
pageSize
```

传给后端，但前端还没有真正提供：

```text
5 / 10 / 20 / 50 条每页
```

的用户选择控件。

因此 `v2` tag 创建前必须补齐：

```text
每页条数选择器
5
10
20
50
```

并在切换 pageSize 时：

```text
currentPage = 1
重新请求列表
```

---

## 2.3 当前工程清理问题

Codex 在建立 Git 仓库之前必须处理以下问题。

当前压缩包中存在不应进入 Git 的内容：

```text
.idea/
target/
node_modules/
.workbuddy/
临时文件
IDE 本地数据源配置
```

必须保证最终仓库不追踪这些目录。

当前后端配置中还存在本地数据库敏感配置。

**禁止把真实数据库密码、Token、密钥、云服务 AccessKey 等提交到 GitHub。**

在第一次 commit 之前完成配置脱敏。

---

# 3. 技术基线

除非出现明确兼容问题，否则不要在重构过程中随意升级版本。

## 3.1 后端

```text
Java                         21
Spring Boot                  4.1.1
Spring MVC
Maven
MyBatis Spring Boot Starter  4.0.0
MySQL Connector/J
HikariCP
MySQL                        8.x
```

## 3.2 前端

当前压缩包基线：

```text
Vue            3.x
Vite           8.x
Axios          1.x
JavaScript
```

暂时保持 JavaScript，不切换 TypeScript。

## 3.3 后续阶段引入

按版本逐步增加：

```text
Bean Validation
SLF4J / Logback
Spring Transaction
Redis
RabbitMQ
WebSocket
Spring Task
Spring Security
JWT
OpenAPI
JUnit
Spring Boot Actuator
GitHub Actions
Nginx
Linux
HTTPS
```

---

# 4. 教学原则

整个项目必须遵守以下原则。

## 4.1 一阶段只解决一类问题

禁止在 `v0` 直接塞入：

```text
Redis
RabbitMQ
Security
Docker
```

禁止在学生还没理解 MyBatis Mapper Proxy 前直接改成 MyBatis-Plus。

---

## 4.2 纵向请求链优先

每个阶段都必须回答：

```text
请求从哪里进入？
经过哪些组件？
数据发生了什么变化？
最终到哪里？
响应怎么返回？
```

例如基础同步链：

```text
Human
↓
Browser
↓
Vue
↓
Axios
↓
HTTP
↓
Tomcat
↓
Spring MVC
↓
Controller
↓
Service
↓
Mapper
↓
MyBatis
↓
JDBC
↓
HikariCP
↓
MySQL
↓
Response
↓
Vue
↓
DOM
↓
Human
```

---

## 4.3 横向能力后引入

等主链跑通之后，再逐步加入：

```text
Validation
Exception
Logging
Transaction
Security
AOP
Metrics
Tracing
```

不要让横切能力遮住主链。

---

## 4.4 每一个版本必须可运行

禁止出现：

```text
v4 只是半成品
v5 才能启动
```

每个 tag 都必须：

```text
后端可启动
前端可启动
数据库脚本完整
核心流程可验证
README 有运行说明
```

---

## 4.5 不制造“假 Git 历史”

Codex 不允许先一次性写完最终代码，然后：

```text
给同一个 commit 打 v0-v10 十一个标签
```

这是无效的。

必须真实形成：

```text
commit(v0)
↓
tag v0
↓
继续开发
↓
commit(v1)
↓
tag v1
...
↓
commit(v10)
↓
tag v10
```

---

# 5. 最终仓库目录规范

目标根目录：

```text
minimall-java-fullstack-evolution/
│
├── README.md
├── CHANGELOG.md
├── .gitignore
├── .editorconfig
│
├── docs/
│   ├── roadmap.md
│   ├── architecture/
│   │   ├── request-chain.md
│   │   ├── data-flow.md
│   │   └── deployment.md
│   │
│   └── stages/
│       ├── v0.md
│       ├── v1.md
│       ├── v2.md
│       ├── v3.md
│       ├── v4.md
│       ├── v5.md
│       ├── v6.md
│       ├── v7.md
│       ├── v8.md
│       ├── v9.md
│       └── v10.md
│
├── database/
│   ├── schema.sql
│   ├── seed.sql
│   └── README.md
│
├── minimall-server/
│   ├── pom.xml
│   └── src/
│
├── minimall-web/
│   ├── package.json
│   └── src/
│
└── .github/
    └── workflows/
        └── ci.yml
```

`ci.yml` 可以到 `v9` 再正式加入。

---

# 6. 根 `.gitignore` 最低要求

必须忽略：

```gitignore
# Java
**/target/
**/.idea/
*.iml

# Node
**/node_modules/
**/dist/

# OS / IDE
.DS_Store
Thumbs.db
.vscode/

# Local config
.env
.env.*
!*.example
**/application-local.properties
**/application-secret.properties

# Runtime
*.log

# Agent / temp
.workbuddy/
tmp/
```

允许保留：

```text
.vscode/extensions.json
```

但不是必须。

---

# 7. 配置与秘密管理

禁止提交：

```text
真实 MySQL 密码
JWT Secret
Redis 密码
RabbitMQ 密码
云服务密钥
服务器 SSH 私钥
```

从 `v1` 开始，建议采用：

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/minimall}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
```

并提供：

```text
docs/configuration.md
```

或：

```text
application-local.properties.example
```

说明本地如何配置。

前端 `VITE_*` 环境变量不能存放真正的服务器秘密。

---

# 8. Git 管理策略

## 8.1 主分支

```text
main
```

`main` 始终代表：

> 最新完成且通过验收的阶段。

Codex 单人执行时不强制每阶段建立 branch。

如果需要阶段开发分支，可使用：

```text
stage/v0-bootstrap
stage/v1-crud
stage/v2-query
...
```

但最终 tag 必须落在合并后的 `main` 上。

---

## 8.2 Commit 规范

推荐 Conventional Commit 风格：

```text
feat:
fix:
refactor:
docs:
test:
chore:
```

阶段完成提交推荐：

```text
feat(v0): establish Spring Boot request chain
feat(v1): complete product CRUD with MyBatis and MySQL
feat(v2): add pagination and multi-condition product query
feat(v3): add unified API contract and exception handling
feat(v4): add order transaction workflow
feat(v5): harden database relationships and stock concurrency
feat(v6): add Redis cache layer
feat(v7): add RabbitMQ asynchronous event flow
feat(v8): add WebSocket push and scheduled tasks
feat(v9): add security testing observability and CI
feat(v10): complete production deployment
```

---

## 8.3 Tag 规范

使用：

```text
v0
v1
v2
v3
v4
v5
v6
v7
v8
v9
v10
```

必须使用 Annotated Tag：

```bash
git tag -a v1 -m "V1 - Basic CRUD with Vue, Spring Boot, MyBatis and MySQL"
```

禁止反复移动已经发布的 tag。

---

## 8.4 每阶段发布流程

阶段验收通过后：

```bash
git add .
git commit -m "feat(vN): ..."
git tag -a vN -m "VN - ..."
git push origin main
git push origin vN
```

若一次推送所有历史 tags：

```bash
git push origin --tags
```

---

# 9. GitHub 初始化要求

仓库：

```text
minimall-java-fullstack-evolution
```

若本地还没有 Git：

```bash
git init -b main
```

第一次 commit 前必须：

```text
清理 node_modules
清理 target
清理 .idea
脱敏 application.properties
检查 git status
```

远程仓库必须是空仓库。

不要让 GitHub 端提前生成与本地冲突的：

```text
README
.gitignore
LICENSE
```

之后添加：

```bash
git remote add origin <REMOTE>
git remote -v
git push -u origin main
```

---

# 10. 每个版本的教程文档统一模板

每一个：

```text
docs/stages/vN.md
```

必须包含：

```text
1. 本阶段目标
2. 上一阶段有什么
3. 本阶段新增什么
4. 纵向请求链
5. 数据形态变化
6. 新增技术的位置
7. 核心目录变化
8. API 变化
9. 数据库变化
10. 手工验证步骤
11. 自动测试
12. 常见错误
13. 本阶段掌握判定
14. 下一阶段为什么存在
15. checkout 当前版本的方法
```

不要把阶段文档写成海量 API 手册。

核心是：

> “这一阶段为什么存在，它在整条 Java 全栈链上解决了什么问题”。

---

# 11. Version Roadmap 总览

| Tag | 阶段 | 核心问题 |
|---|---|---|
| `v0` | 请求链认知 | 一个请求怎样进入 Spring Boot 并返回 |
| `v1` | 基础 CRUD | Java 如何真正操作 MySQL |
| `v2` | 查询能力 | 分页、模糊查询、多条件动态 SQL |
| `v3` | 工程化基础 | Result、Validation、Exception、Logging |
| `v4` | 真实业务与事务 | Service 如何组织多 SQL 业务 |
| `v5` | 数据关系与并发 | JOIN、索引、库存并发、锁 |
| `v6` | Redis | 缓存链怎样接入同步主链 |
| `v7` | RabbitMQ | 同步业务如何拆出异步支线 |
| `v8` | WebSocket + Task | 实时推送与时间驱动入口 |
| `v9` | Security + 工程质量 | 认证授权、测试、文档、监控、CI |
| `v10` | 生产部署 | Nginx、Linux、HTTPS、构建与发布 |

---

# 12. V0 — Spring Boot / Java Web 请求链认知

## 12.1 Tag

```text
v0
```

## 12.2 阶段定位

V0 不追求商城功能。

目标只有一个：

> 让学习者亲手看到一个请求怎样从浏览器进入 Java，再返回浏览器。

---

## 12.3 必须覆盖的链

```text
Browser
↓
HTTP
↓
Spring Boot
↓
Embedded Tomcat
↓
DispatcherServlet
↓
HandlerMapping
↓
Controller
↓
HttpMessageConverter / Jackson
↓
HTTP Response
↓
Browser
```

前端加入后：

```text
Human Input
↓
Vue
↓
JavaScript Object
↓
Axios
↓
JSON
↓
HTTP
↓
Spring MVC
↓
DTO
↓
Controller
↓
JSON
↓
Vue
↓
DOM
```

---

## 12.4 功能目标

必须包含：

```text
GET /api/hello
GET /api/info
GET Query Parameter 示例
GET PathVariable 示例
POST @RequestBody DTO 示例
```

前端必须有一个最小 Vue 表单：

```text
输入数据
↓
Axios POST
↓
后端 DTO
↓
Echo Response
↓
页面显示
```

使用 Vite Proxy 解决开发环境 `/api` 转发。

---

## 12.5 禁止内容

V0 不允许引入：

```text
MySQL
MyBatis
Redis
Security
MQ
```

---

## 12.6 学习成果

学习者能够解释：

```text
@SpringBootApplication
@RestController
@RequestMapping
@GetMapping
@PostMapping
@RequestParam
@PathVariable
@RequestBody
DTO
Jackson
JSON
HTTP 200 / 400 / 404 / 415
Tomcat
DispatcherServlet
```

---

## 12.7 验收标准

```text
后端能启动在 8080
Vue 能启动在 5173
前后端能通过 /api 联调
POST JSON 能变成 DTO
Java 对象能变成 JSON
Chrome Network 可观察请求
```

---

# 13. V1 — 基础商品 CRUD

## 13.1 Tag

```text
v1
```

## 13.2 代码依据

主要参考：

```text
基础CRUDminimall.zip
```

但必须先做仓库清理与配置脱敏。

---

## 13.3 核心目标

从：

```text
Controller Echo
```

升级为：

```text
Vue
↓
Controller
↓
Service
↓
Mapper
↓
MyBatis
↓
JDBC
↓
HikariCP
↓
MySQL
```

---

## 13.4 数据库

至少：

```text
product
```

字段：

```text
id
name
category_id
price
stock
status
description
create_time
update_time
```

金额使用：

```text
DECIMAL
↔
BigDecimal
```

---

## 13.5 后端目标

建立：

```text
controller/
dto/
entity/
service/
service/impl/
mapper/
resources/mapper/
```

实现：

```text
POST   /api/products
GET    /api/products/{id}
GET    /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

---

## 13.6 前端目标

商品管理页面必须支持：

```text
查询全部
根据 ID 查询
新增
编辑回填
修改
删除
```

---

## 13.7 重点知识

```text
DTO != Entity

ServiceImpl
Mapper Interface
Mapper Proxy
Mapper XML
namespace
statement id
#{}
JDBC
DataSource
HikariCP
MySQL Driver
```

必须在阶段文档里解释：

> 为什么 Mapper 没有 MapperImpl。

---

## 13.8 验收

必须从页面完成：

```text
新增 → MySQL 能看到
修改 → MySQL 真修改
删除 → MySQL 记录消失
ID 查询 → 返回正确商品
列表查询 → 展示数据库数据
```

---

# 14. V2 — 分页、模糊查询与多条件组合

## 14.1 Tag

```text
v2
```

## 14.2 代码依据

主要参考：

```text
条件分页模糊查询minimall.zip
```

---

## 14.3 后端目标

增加：

```text
ProductQueryDTO
PageResult<T>
```

支持：

```text
page
pageSize
name
status
minPrice
maxPrice
```

---

## 14.4 SQL 能力

必须真实使用：

```text
COUNT(*)
WHERE
LIKE
AND
ORDER BY
LIMIT
OFFSET
```

MyBatis：

```text
<where>
<if>
```

---

## 14.5 分页公式

```text
offset = (page - 1) * pageSize
```

---

## 14.6 前端要求

必须包含：

```text
商品名称模糊搜索
状态选择
最低价格
最高价格
查询
重置
上一页
下一页
当前页
总页数
总记录数
```

### 必须补齐当前压缩包缺少的功能

```text
每页 5 条
每页 10 条
每页 20 条
每页 50 条
```

切换 pageSize：

```text
currentPage = 1
重新请求
```

---

## 14.7 验收

至少验证：

```text
默认分页
name 模糊查询
status 查询
价格范围查询
多条件组合
pageSize 切换
翻页后条件保持
```

---

# 15. V3 — 工程化基础

## 15.1 Tag

```text
v3
```

## 15.2 核心目标

把：

```text
能跑 CRUD
```

升级成：

```text
有统一 API 契约的 CRUD
```

---

## 15.3 后端新增

```text
Result<T>
Bean Validation
BusinessException
GlobalExceptionHandler
SLF4J Logging
```

建议目录：

```text
common/
exception/
```

---

## 15.4 统一响应

例如：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

错误：

```json
{
  "code": 404,
  "message": "商品不存在",
  "data": null
}
```

HTTP 状态码与错误语义必须一致。

---

## 15.5 Validation

至少：

```text
@NotBlank
@NotNull
@Min
@DecimalMin
@Valid
```

必须验证：

```text
空商品名称
负价格
负库存
非法 page
非法 pageSize
```

---

## 15.6 Exception

必须区分：

```text
业务异常
参数异常
JSON 格式异常
系统异常
```

统一进入：

```text
@RestControllerAdvice
```

---

## 15.7 Logging

禁止业务代码继续使用：

```text
System.out.println
printStackTrace
```

改为：

```text
INFO
WARN
ERROR
```

---

## 15.8 前端工程化

开始拆分：

```text
src/
├── api/
│   ├── http.js
│   └── product.js
│
├── views/
│   └── ProductView.vue
│
├── components/
└── App.vue
```

Axios 统一处理：

```text
Result<T>
后端 message
HTTP error
```

---

## 15.9 配置安全

开始采用：

```text
环境变量
local profile
prod profile
```

不得提交数据库真实密码。

---

# 16. V4 — Order 真实业务 + Transaction

## 16.1 Tag

```text
v4
```

## 16.2 核心目的

让 Service 第一次真正承担“业务”。

新增：

```text
orders
order_item
```

---

## 16.3 创建订单链

```text
Vue
↓
OrderCreateDTO
↓
OrderController
↓
OrderService
│
├── 查询商品
├── 校验商品状态
├── 校验库存
├── 计算总金额
├── 创建订单
├── 创建订单项
└── 扣减库存
↓
多个 Mapper
↓
多个 SQL
↓
MySQL
```

---

## 16.4 Transaction

必须使用：

```text
@Transactional
```

但必须通过真实业务说明它存在的原因：

```text
INSERT orders
↓
INSERT order_item
↓
UPDATE stock
```

任一步失败：

```text
ROLLBACK
```

全部成功：

```text
COMMIT
```

---

## 16.5 必须做回滚实验

提供一个教学用失败开关或测试：

```text
订单写入成功
↓
故意在扣库存前抛异常
↓
验证订单和明细全部回滚
```

这是 v4 的核心验收。

---

## 16.6 前端

此阶段开始加入 Vue Router：

```text
/products
/orders
```

订单页面至少：

```text
创建订单
订单列表
订单详情
```

---

# 17. V5 — 数据关系、索引与库存并发

## 17.1 Tag

```text
v5
```

## 17.2 核心目标

从：

```text
单线程正确
```

升级成：

```text
数据关系正确
并发下也尽量正确
```

---

## 17.3 数据关系

补齐：

```text
category
product
orders
order_item
```

关系：

```text
Category 1 --- N Product

Order 1 --- N OrderItem

Product 1 --- N OrderItem
```

---

## 17.4 JOIN

订单详情至少出现真实：

```text
JOIN
```

让学习者看到：

```text
多个表
↓
ResultSet
↓
VO
```

---

## 17.5 索引

明确设计：

```text
PRIMARY KEY
普通索引
组合索引
唯一索引（若业务需要）
```

必须用：

```text
EXPLAIN
```

观察至少一条查询。

---

## 17.6 库存并发

不能继续：

```text
SELECT stock
↓
Java 判断
↓
UPDATE stock
```

作为唯一防线。

推荐加入原子库存扣减：

```sql
UPDATE product
SET stock = stock - ?
WHERE id = ?
  AND stock >= ?
```

根据：

```text
affected rows
```

判断是否扣减成功。

可进一步增加：

```text
version
```

展示乐观锁。

---

## 17.7 教程必须解释

```text
原子性
隔离级别
MVCC
行锁
乐观锁
悲观锁
```

实现不必把所有锁方案都用于业务，至少真正落地一种并发保护方案。

---

# 18. V6 — Redis 缓存支线

## 18.1 Tag

```text
v6
```

## 18.2 核心链

```text
Controller
↓
Service
↓
Redis
↓
Hit?
├─ Yes → Return
└─ No
   ↓
 MySQL
   ↓
 Redis
   ↓
 Return
```

---

## 18.3 缓存对象

优先缓存：

```text
商品详情
分类信息
```

不要把高频变动库存简单粗暴长期缓存。

---

## 18.4 必须实现

```text
Cache Aside
TTL
缓存命中
缓存未命中
更新后缓存失效
删除后缓存失效
```

---

## 18.5 必须观察

日志需要能够区分：

```text
CACHE HIT
CACHE MISS
DB QUERY
CACHE EVICT
```

---

## 18.6 教程概念

简单介绍：

```text
缓存穿透
缓存击穿
缓存雪崩
```

其中至少落地一个基础保护措施。

---

# 19. V7 — RabbitMQ 异步消息

## 19.1 Tag

```text
v7
```

## 19.2 核心目的

第一次把同步主链拆出异步支线。

```text
OrderService
↓
订单提交成功
↓
OrderCreatedEvent
↓
Producer
↓
RabbitMQ
↓
Consumer
↓
异步处理
```

---

## 19.3 异步任务

消费者可以实现：

```text
订单通知
操作审计
模拟短信
模拟邮件
```

不要引入真实付费短信服务。

---

## 19.4 必须讨论

```text
Producer
Exchange
Queue
Binding
Consumer
ACK
Retry
Dead Letter
Idempotency
```

---

## 19.5 事务边界

不能让：

```text
数据库事务最终回滚
```

但：

```text
消息已经宣布“订单成功”
```

阶段实现应保证消息发布时机与事务提交结果一致。

---

# 20. V8 — WebSocket + Spring Task

## 20.1 Tag

```text
v8
```

这一阶段专门展示：

> Web 应用并不只有 Controller 这一种入口。

---

## 20.2 WebSocket

链：

```text
Order status changed
↓
Server
↓
WebSocket
↓
Browser
↓
实时刷新 / 通知
```

后台页面需要：

```text
实时订单通知
连接状态显示
```

---

## 20.3 Spring Task

时间驱动入口：

```text
Clock
↓
@Scheduled
↓
OrderService
↓
查询超时订单
↓
关闭订单
```

这条链没有：

```text
Browser
Controller
```

必须在教程中明确标出。

---

## 20.4 验收

```text
浏览器建立 WebSocket
后台发生订单事件
页面无需主动刷新即可得到通知

创建待支付订单
等待 / 模拟超过超时时间
定时任务自动关闭订单
```

---

# 21. V9 — Security + 工程质量

## 21.1 Tag

```text
v9
```

## 21.2 Spring Security

加入：

```text
登录
JWT
FilterChain
身份认证
角色授权
```

角色最低：

```text
ADMIN
USER
```

---

## 21.3 安全链

```text
HTTP Request
↓
Security FilterChain
↓
JWT Filter
↓
Authentication
↓
Authorization
↓
DispatcherServlet
↓
Controller
```

---

## 21.4 权限示例

```text
USER
→ 浏览商品
→ 创建自己的订单

ADMIN
→ 商品管理
→ 订单管理
```

---

## 21.5 AOP

增加操作日志：

```text
管理员新增商品
管理员修改商品
管理员删除商品
```

用：

```text
AOP
```

实现横切日志，不把日志模板代码复制到每个 Controller。

---

## 21.6 OpenAPI

生成接口文档。

文档需覆盖：

```text
请求参数
响应结构
HTTP 状态码
认证要求
```

---

## 21.7 Testing

至少建立：

```text
Service Unit Test
Controller Integration Test
Mapper Integration Test
Transaction Rollback Test
Security Test
```

---

## 21.8 Actuator / Observability

加入基础：

```text
health
metrics
```

不要求在教学项目里建设完整 Prometheus/Grafana 平台，但要说明位置。

---

## 21.9 GitHub Actions

正式加入：

```text
.github/workflows/ci.yml
```

CI 至少执行：

```text
Backend:
Maven test
Maven package

Frontend:
npm ci
npm run build
```

---

# 22. V10 — Production Deployment

## 22.1 Tag

```text
v10
```

## 22.2 核心目标

把：

```text
localhost 开发项目
```

变成：

```text
真实 Linux 服务
```

---

## 22.3 必须采用的基础部署模型

```text
Internet
↓
HTTPS
↓
Nginx
├── Vue dist
└── /api
      ↓
Spring Boot :8080
      ↓
MySQL
      ↓
Redis
      ↓
RabbitMQ
```

---

## 22.4 前端

```bash
npm ci
npm run build
```

生成：

```text
dist/
```

由 Nginx 提供静态资源。

---

## 22.5 后端

```bash
mvn clean test
mvn clean package
```

生成：

```text
jar
```

使用 Linux 服务管理。

推荐：

```text
systemd
```

管理：

```text
启动
停止
重启
开机自启
日志
```

---

## 22.6 Nginx

必须实现：

```text
静态资源托管
/api 反向代理
HTTPS
```

生产环境前端不再依赖：

```text
Vite Dev Server
```

---

## 22.7 配置

生产环境所有秘密使用：

```text
Environment Variables
```

禁止打进源码或 jar 配置。

---

## 22.8 HTTPS

配置合法证书。

HTTP 强制跳转 HTTPS。

---

## 22.9 健康检查

利用：

```text
Actuator health
```

验证后端状态。

---

## 22.10 Docker

本教程主线优先完成：

```text
Linux + Nginx + jar
```

作为最容易理解实际进程关系的部署方式。

Docker 作为：

```text
Optional Extension
```

而不是 v10 必须条件。

---

# 23. 各版本 Tag 信息

推荐 annotated tag message：

```text
v0  MiniMall V0 - Spring Boot request chain
v1  MiniMall V1 - Basic full-stack CRUD
v2  MiniMall V2 - Pagination and dynamic query
v3  MiniMall V3 - Engineering API foundation
v4  MiniMall V4 - Order workflow and transaction
v5  MiniMall V5 - Data relationships and concurrency
v6  MiniMall V6 - Redis cache
v7  MiniMall V7 - RabbitMQ asynchronous messaging
v8  MiniMall V8 - WebSocket and scheduled tasks
v9  MiniMall V9 - Security quality and CI
v10 MiniMall V10 - Production deployment
```

---

# 24. 每个 Tag 创建前统一质量门禁

Codex 每次准备打 tag 前必须执行。

## 24.1 Backend

```bash
mvn test
mvn package
```

必须：

```text
BUILD SUCCESS
```

---

## 24.2 Frontend

```bash
npm ci
npm run build
```

必须成功。

---

## 24.3 Git

```bash
git status
```

检查：

```text
无 node_modules
无 target
无 IDE 垃圾
无明文 secret
无临时文件
```

---

## 24.4 Manual Smoke Test

对应阶段核心链必须人工 / 脚本验证。

例如 V2：

```text
新增商品
按 ID 查询
分页
模糊查询
多条件查询
修改
删除
```

---

# 25. README 最终要求

根 `README.md` 必须首先展示：

```text
MiniMall Java Full-Stack Evolution
```

并明确：

> 这是一个“通过 Git tags 学习 Java 全栈系统演进”的教程项目。

必须有版本表：

| Version | Topic | Checkout |
|---|---|---|
| v0 | Spring Boot Request Chain | `git checkout v0` |
| v1 | CRUD | `git checkout v1` |
| ... | ... | ... |
| v10 | Deployment | `git checkout v10` |

并包含：

```text
项目目标
技术栈
最终架构
快速运行
版本学习路径
目录结构
数据库初始化
环境变量
Git tag 使用
```

---

# 26. CHANGELOG 要求

每个版本记录：

```text
Added
Changed
Fixed
Learning Focus
```

例如：

```markdown
## v2

### Added
- ProductQueryDTO
- PageResult
- Dynamic SQL
- pageSize selector

### Learning Focus
- LIMIT / OFFSET
- LIKE
- MyBatis <if>
- MyBatis <where>
```

---

# 27. Codex 执行顺序

Codex 不得直接从当前代码跳到 v10。

必须严格：

```text
Step 0
创建干净 monorepo

↓
重建 v0
验收
commit
tag v0

↓
基于基础 CRUD 压缩包整理 v1
验收
commit
tag v1

↓
基于分页查询压缩包整理 v2
补 pageSize selector
验收
commit
tag v2

↓
实现 v3
验收
tag

...

↓
实现 v10
最终验收
tag
```

---

# 28. Codex 必须保留的教学痕迹

这个项目不是纯“最终代码”。

因此每个版本必须保留足够教学可见性。

例如 V1：

```text
不要直接改成 MyBatis-Plus
不要隐藏 Mapper Proxy 概念
不要删除 XML
```

V4：

```text
必须能看到多 Mapper 调用
必须能观察 transaction rollback
```

V6：

```text
必须能在日志中观察 cache hit / miss
```

V7：

```text
必须能观察 producer / queue / consumer
```

V8：

```text
必须能看到 WebSocket 与 @Scheduled 是不同入口
```

---

# 29. 非目标 / 防止项目失控

主线暂不引入：

```text
微服务
Spring Cloud
Kubernetes
复杂 DDD
分库分表
Elasticsearch
真实支付
真实短信
复杂前端组件库体系
```

这些可以放在：

```text
Future Roadmap
```

但不属于 v0-v10 必做内容。

---

# 30. 最终完整技术地图

完成 v10 后，系统应能够展示：

```text
Browser
↓
Vue
↓
Axios
↓
Nginx
↓
Spring Security FilterChain
↓
Spring MVC
↓
Controller
↓
Validation
↓
Service
├── Transaction
├── Redis
├── MQ Producer
├── WebSocket Push
└── External Capability
↓
Mapper
↓
MyBatis
↓
JDBC / HikariCP
↓
MySQL
```

旁路线：

```text
RabbitMQ
Producer → Queue → Consumer
```

时间入口：

```text
Clock
↓
@Scheduled
↓
Service
```

横切：

```text
Security
Validation
AOP
Transaction
Exception
Logging
Metrics
```

工程外围：

```text
Maven
Git
Git Tags
JUnit
OpenAPI
GitHub Actions
Linux
Nginx
HTTPS
Deployment
```

---

# 31. 最终验收标准

项目最终不能只通过“功能能点”。

必须满足四个维度。

## 31.1 功能

```text
商品
订单
CRUD
分页
查询
缓存
消息
实时通知
定时任务
登录授权
```

---

## 31.2 架构

能够清晰指出：

```text
Controller
Service
Mapper
DTO
Entity
VO
Filter
Interceptor / MVC
AOP
Transaction
Redis
MQ
WebSocket
Task
```

各自在请求链上的位置。

---

## 31.3 工程

```text
统一响应
异常处理
Validation
Logging
Test
OpenAPI
CI
环境隔离
Secrets
```

---

## 31.4 运维

```text
前端 build
后端 jar
Linux
Nginx
HTTPS
systemd
Health Check
```

---

# 32. 给 Codex 的最终指令

> 以本文件为唯一阶段路线规范。
>
> 读取用户提供的两个 MiniMall 压缩包，分别作为 V1 与 V2 的真实阶段参考。
>
> 不直接在压缩包内继续堆代码，而是新建一个干净 monorepo：`minimall-java-fullstack-evolution`。
>
> 清除 node_modules、target、.idea、临时文件、Agent 临时目录和所有敏感配置。
>
> 从 V0 开始真实重建 Git 历史。
>
> 每一阶段完成后必须：
>
> 1. 更新对应 `docs/stages/vN.md`
> 2. 更新 README 版本表
> 3. 更新 CHANGELOG
> 4. 后端测试通过
> 5. 前端 build 通过
> 6. 检查无 secret
> 7. commit
> 8. annotated tag
> 9. push main
> 10. push tag
>
> 不允许把 v0-v10 全部指向同一 commit。
>
> 不允许为了“代码高级”提前跳过教学链路。
>
> 每一版本首先服务于“理解这一阶段新增的链路”，其次才是代码抽象程度。
>
> 如果现有代码与本规范存在冲突：
>
> - 功能行为以本规范为目标；
> - V1/V2 的历史教学内容尽量保留；
> - 明显 bug、安全问题、secret、无效依赖和构建垃圾必须修复；
> - 不允许把未来版本能力提前塞进早期 tag。
>
> 最终 GitHub 仓库名称固定为：
>
> `minimall-java-fullstack-evolution`
>
> 最终必须能够通过 `git checkout v0` 到 `git checkout v10` 顺序学习整个 Java 全栈项目的演进过程。
