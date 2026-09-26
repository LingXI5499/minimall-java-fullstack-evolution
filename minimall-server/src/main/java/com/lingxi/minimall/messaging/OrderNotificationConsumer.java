package com.lingxi.minimall.messaging;

import com.lingxi.minimall.mapper.OrderNotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/** RabbitMQ 消费入口：写一条模拟通知记录，成功返回后由容器 ACK。 */
@Component
public class OrderNotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderNotificationConsumer.class);
    private final OrderNotificationMapper mapper;
    private final ObjectMapper json;
    public OrderNotificationConsumer(OrderNotificationMapper mapper, ObjectMapper json) {
        this.mapper = mapper;
        this.json = json;
    }

    @RabbitListener(queues = RabbitConfiguration.QUEUE)
    @Transactional
    public void consume(String payload) {
        OrderCreatedEvent event = json.readValue(payload, OrderCreatedEvent.class);
        int inserted = mapper.insertIfAbsent(event.orderId(), "订单 #" + event.orderId() + " 已创建（模拟通知）");
        if (inserted == 0) {
            log.info("MQ CONSUMER duplicate orderId={}, skipped", event.orderId());
            return;
        }
        log.info("MQ CONSUMER notified orderId={}", event.orderId());
    }
}
