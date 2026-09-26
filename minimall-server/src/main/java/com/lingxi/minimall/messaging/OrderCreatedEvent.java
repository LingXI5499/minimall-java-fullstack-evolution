package com.lingxi.minimall.messaging;

/** 订单事务中产生的领域事件；监听器只会在提交成功后把它发到 RabbitMQ。 */
public record OrderCreatedEvent(Long orderId) {}
