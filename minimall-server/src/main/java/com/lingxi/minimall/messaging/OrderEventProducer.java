package com.lingxi.minimall.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tools.jackson.databind.ObjectMapper;

/** 事务提交后才向 RabbitMQ 发布订单创建消息，避免“订单回滚却通知成功”。 */
@Component
public class OrderEventProducer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private final RabbitTemplate rabbit;
    private final ObjectMapper json;
    public OrderEventProducer(RabbitTemplate rabbit, ObjectMapper json) { this.rabbit = rabbit; this.json = json; }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(OrderCreatedEvent event) {
        try {
            rabbit.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.ROUTING_KEY,
                    json.writeValueAsString(event));
            log.info("MQ PRODUCER order.created orderId={}", event.orderId());
        } catch (RuntimeException e) {
            // 数据库已经提交，不能再把订单回滚；生产级可靠投递可在后续引入 Outbox。
            log.error("MQ publish failed after order commit orderId={}", event.orderId(), e);
        }
    }
}
