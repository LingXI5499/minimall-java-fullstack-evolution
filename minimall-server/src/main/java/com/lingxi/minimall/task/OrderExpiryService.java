package com.lingxi.minimall.task;

import com.lingxi.minimall.cache.ProductCache;
import com.lingxi.minimall.entity.Order;
import com.lingxi.minimall.mapper.OrderItemMapper;
import com.lingxi.minimall.mapper.OrderMapper;
import com.lingxi.minimall.mapper.ProductMapper;
import com.lingxi.minimall.realtime.OrderWebSocketHandler;
import com.lingxi.minimall.vo.OrderLineVO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** 超时订单关闭的业务逻辑，与 @Scheduled 入口分开，保证事务代理生效。 */
@Service
public class OrderExpiryService {
    private static final Logger log = LoggerFactory.getLogger(OrderExpiryService.class);
    private final OrderMapper orders;
    private final OrderItemMapper items;
    private final ProductMapper products;
    private final ProductCache cache;
    private final OrderWebSocketHandler websocket;
    private final long timeoutSeconds;

    public OrderExpiryService(OrderMapper orders, OrderItemMapper items, ProductMapper products,
            ProductCache cache, OrderWebSocketHandler websocket,
            @Value("${app.orders.timeout-seconds:1800}") long timeoutSeconds) {
        this.orders = orders;
        this.items = items;
        this.products = products;
        this.cache = cache;
        this.websocket = websocket;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Transactional
    public int closeExpired() {
        LocalDateTime cutoff = LocalDateTime.now().minusSeconds(timeoutSeconds);
        List<Order> expired = orders.selectExpired(cutoff);
        List<Long> closedIds = new ArrayList<>();
        List<Long> changedProductIds = new ArrayList<>();
        for (Order order : expired) {
            if (orders.closePending(order.getId()) != 1) continue;
            // 库存是在创建待处理订单时扣的；关闭订单必须在同一事务中归还。
            for (OrderLineVO item : items.selectDetailByOrderId(order.getId())) {
                if (products.increaseStock(item.getProductId(), item.getQuantity()) == 0) {
                    log.warn("Cannot restore stock for deleted productId={} orderId={}", item.getProductId(), order.getId());
                } else {
                    changedProductIds.add(item.getProductId());
                }
            }
            closedIds.add(order.getId());
        }
        if (!closedIds.isEmpty()) {
            // 外部通知和缓存失效都等数据库提交成功后再做。
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() {
                    changedProductIds.forEach(cache::evict);
                    closedIds.forEach(id -> websocket.broadcast("ORDER_CLOSED", id));
                }
            });
        }
        return closedIds.size();
    }
}
