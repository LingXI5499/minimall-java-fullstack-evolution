# 数据流与事务边界

## 商品读取与修改

商品详情先读 Redis；没有缓存时读 MySQL 并设置 TTL。查不到的 ID 也短暂缓存空值。创建、更新、删除商品成功后使相关缓存失效。缓存丢失只会增加数据库读取，不应改变商品事实。

## 创建订单

```text
订单 DTO → 校验用户和商品 → 条件扣库存 → 插入 orders → 插入 order_item
                                   同一 MySQL 事务
提交成功 → 发布 RabbitMQ 事件 / 推送 WebSocket / 清理商品缓存
回滚     → 上述提交后动作均不执行
```

订单头保存 `owner_username`。USER 只能看自己创建的订单，ADMIN 可查看全部。历史订单没有 owner，只能由 ADMIN 查看。库存用 `stock >= quantity` 的条件 UPDATE；受影响行数为零时返回库存不足。

RabbitMQ 消费者把通知写入 `order_notification`，数据库唯一键防止重复事件产生重复通知。生产者在数据库提交后发送消息，因此 MQ 故障不会回滚已成功的订单；当前实现没有 Outbox，若提交后发布失败，需要日志、补偿或后续引入持久化 Outbox，不能把它误认为端到端必达。

## 到期关闭

`OrderExpiryTask` 定期扫描待处理且超时的订单，`OrderExpiryService` 在事务内把状态改为 CLOSED 并恢复库存。只有真正从待处理转为关闭时才恢复一次；事务提交后推送 WebSocket 事件、使商品缓存失效。前端收到事件后重新获取列表，列表和库存最终以数据库为准。
