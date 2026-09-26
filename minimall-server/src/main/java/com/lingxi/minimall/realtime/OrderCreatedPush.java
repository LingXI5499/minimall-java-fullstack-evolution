package com.lingxi.minimall.realtime;

import com.lingxi.minimall.messaging.OrderCreatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** 订单真正提交后才通知浏览器，避免页面看到已回滚的订单。 */
@Component
public class OrderCreatedPush {
    private final OrderWebSocketHandler websocket;
    public OrderCreatedPush(OrderWebSocketHandler websocket) { this.websocket = websocket; }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(OrderCreatedEvent event) {
        websocket.broadcast("ORDER_CREATED", event.orderId());
    }
}
