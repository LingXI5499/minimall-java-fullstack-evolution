# Linux 部署：Nginx + HTTPS + Spring Boot jar

## 进程关系

```text
Internet → HTTPS :443 → Nginx ┬→ /var/www/minimall 的 Vue dist
                              ├→ /api → 127.0.0.1:8080 Spring Boot
                              └→ /ws  → 127.0.0.1:8080 WebSocket
                                                 ├→ MySQL 8
                                                 ├→ Redis
                                                 └→ RabbitMQ
```

以下命令以带 `sudo` 权限的 Linux 用户、可用的 DNS 域名为前提。将 `mall.example.com` 换成自己的域名。服务可以与数据库在同一台机器，也可通过环境变量连接独立服务器；数据库、Redis、RabbitMQ 不应向公网开放。

## 1. 准备依赖和数据库

安装 JDK 21、MySQL 8、Redis、RabbitMQ、Nginx、Certbot，以及构建机器上的 Maven、Node.js。给应用分别创建最小权限的 MySQL 和 RabbitMQ 账号，并配置 Redis 密码。按 [数据库说明](../../database/README.md) 初始化：**新库**依次执行 `schema.sql`、V5/V7/V9 迁移，最后按需执行一次 `seed.sql`；**已有库**只执行尚未运行的迁移，先备份。

让 DNS A/AAAA 记录指向服务器，开放 80/443 端口，保持 8080 只监听本机。先申请正式证书，再启用引用证书文件的 Nginx 配置。例如在尚未启用该 HTTPS 配置时使用 `sudo certbot certonly --standalone -d mall.example.com`；确保 80 端口未被 Nginx 占用，并设置证书自动续期。证书路径与域名按实际结果调整。

## 2. 构建并安装

```bash
cd minimall-server
mvn clean test
mvn clean package
cd ../minimall-web
npm ci
npm run build
```

在服务器创建系统用户与目录，并复制构建产物。`target` 中的 jar 名为 `minimall-server-0.0.1-SNAPSHOT.jar`；复制命令中的 `REPO` 代表源码目录。

```bash
sudo useradd --system --home /opt/minimall --shell /usr/sbin/nologin minimall
sudo install -d -o minimall -g minimall /opt/minimall
sudo install -d /etc/minimall /var/www/minimall
sudo install -o minimall -g minimall REPO/minimall-server/target/minimall-server-0.0.1-SNAPSHOT.jar /opt/minimall/minimall-server.jar
sudo cp -a REPO/minimall-web/dist/. /var/www/minimall/
sudo cp REPO/deploy/minimall.env.example /etc/minimall/minimall.env
sudo chmod 600 /etc/minimall/minimall.env
sudo cp REPO/deploy/minimall.service /etc/systemd/system/minimall.service
sudo cp REPO/deploy/nginx.conf.example /etc/nginx/sites-available/minimall.conf
sudo ln -s /etc/nginx/sites-available/minimall.conf /etc/nginx/sites-enabled/minimall.conf
```

首次运行 `useradd`、`ln -s` 即可；后续发布只替换 jar 与 dist。编辑 `/etc/minimall/minimall.env`，填入各服务的真实连接信息和随机密码；`JWT_SECRET` 至少 32 个 UTF-8 字节，管理员和普通用户密码各至少 12 位。模板中的 `replace-with-...` 只是占位符，不可原样用于生产。修改 Nginx 示例中的域名与证书路径，修改 `WS_ALLOWED_ORIGINS` 为对应的 `https://域名`。秘密留在服务器的环境文件，不能提交到 Git。

## 3. 启动与验收

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now minimall
sudo systemctl status minimall
sudo journalctl -u minimall -n 100 --no-pager
sudo nginx -t
sudo systemctl reload nginx
curl -i http://127.0.0.1:8080/actuator/health
curl -I http://mall.example.com/
curl -I https://mall.example.com/
```

健康检查应返回 200 和 `UP`；如果 MySQL、Redis 或 RabbitMQ 状态异常，先检查依赖和 `journalctl`。HTTP 请求应得到 301 跳转，HTTPS 应返回前端页面。浏览器再验证登录、商品、订单和 WebSocket；刷新 `/orders` 仍应显示页面。生产 profile 关闭 Swagger 页面；Nginx 不转发 `/actuator/`，健康检查从本机端口访问。

发布新版时先备份当前 jar 和数据库，运行测试并构建，再替换 jar、dist，执行 `sudo systemctl restart minimall` 和上述检查。回滚时恢复旧 jar/dist，数据库迁移需按实际变更制定回滚方案；不要盲目回退已执行的迁移。项目当前没有服务器地址或域名，故仓库提供可部署配置，线上生效需要在目标 Linux 主机实际执行本页步骤。
