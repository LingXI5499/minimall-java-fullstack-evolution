# 本地配置

默认连接 `jdbc:mysql://localhost:3306/minimall`，用户名 `root`，密码为空。若本机不同，请在启动后端的终端设置 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 环境变量。不要将密码写入 Git 追踪文件。

也可以使用被 Git 忽略的 `application-local.properties`，并通过 `--spring.profiles.active=local` 启动。
