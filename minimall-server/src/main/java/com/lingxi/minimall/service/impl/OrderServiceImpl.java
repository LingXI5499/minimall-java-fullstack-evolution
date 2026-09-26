package com.lingxi.minimall.service.impl;

import com.lingxi.minimall.dto.OrderCreateDTO;
import com.lingxi.minimall.dto.OrderLineDTO;
import com.lingxi.minimall.cache.ProductCache;
import com.lingxi.minimall.entity.Order;
import com.lingxi.minimall.entity.OrderItem;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.exception.BusinessException;
import com.lingxi.minimall.mapper.OrderItemMapper;
import com.lingxi.minimall.mapper.OrderMapper;
import com.lingxi.minimall.mapper.ProductMapper;
import com.lingxi.minimall.messaging.OrderCreatedEvent;
import com.lingxi.minimall.service.OrderService;
import com.lingxi.minimall.vo.OrderDetailVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 订单创建的业务编排。Controller 只接收请求；这里负责查商品、算金额、写两张订单表和扣库存。
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderMapper orders;
    private final OrderItemMapper items;
    private final ProductMapper products;
    private final ProductCache cache;
    private final ApplicationEventPublisher events;
    private final boolean rollbackEnabled;

    public OrderServiceImpl(OrderMapper orders, OrderItemMapper items, ProductMapper products, ProductCache cache,
                            ApplicationEventPublisher events,
                            @Value("${app.demo.rollback-enabled:false}") boolean rollbackEnabled) {
        this.orders = orders;
        this.items = items;
        this.products = products;
        this.cache = cache;
        this.events = events;
        this.rollbackEnabled = rollbackEnabled;
    }

    /**
     * 一个事务包住所有写操作。若后续任一步抛出运行时异常，Spring 会回滚已经执行的 SQL。
     * 这个事务只覆盖数据库；未来接入 MQ 时必须等提交成功后再通知外部系统。
     */
    @Override
    @Transactional
    public OrderDetailVO create(OrderCreateDTO request) {
        if (request.simulateFailure() && !rollbackEnabled) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "回滚演示未启用");
        }
        Set<Long> seen = new HashSet<>();
        List<OrderItem> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLineDTO line : request.items()) {
            // 不接受同一商品重复出现，避免分两行检查库存时合计数量超过实际库存。
            if (!seen.add(line.productId())) throw new BusinessException(HttpStatus.BAD_REQUEST, "订单中商品不能重复");
            Product product = products.selectById(line.productId());
            // 单价由数据库取得，不能信任浏览器提交的金额。
            if (product == null) throw new BusinessException(HttpStatus.NOT_FOUND, "商品不存在");
            if (!Integer.valueOf(1).equals(product.getStatus())) throw new BusinessException(HttpStatus.CONFLICT, "商品已下架");
            if (product.getStock() < line.quantity()) throw new BusinessException(HttpStatus.CONFLICT, "库存不足");
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setQuantity(line.quantity());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
            lines.add(item);
            total = total.add(item.getSubtotal());
        }
        Order order = new Order();
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        order.setOwnerUsername(currentUser().getName());
        orders.insert(order);
        // 先写订单头取得自增 ID，明细用这个 ID 建立关联。
        for (OrderItem item : lines) {
            item.setOrderId(order.getId());
            items.insert(item);
        }
        // 教学开关只在显式启用时可用：此时订单与明细已写入，但异常会让两者一起回滚。
        if (request.simulateFailure()) throw new IllegalStateException("教学回滚：明细已插入，库存扣减前抛异常");
        // 多商品订单按 ID 固定扣减顺序，减少并发事务互相等待形成死锁的机会。
        for (OrderItem item : lines.stream().sorted((a, b) -> a.getProductId().compareTo(b.getProductId())).toList()) {
            if (products.decreaseStock(item.getProductId(), item.getQuantity()) != 1) {
                throw new BusinessException(HttpStatus.CONFLICT, "库存不足或商品已下架");
            }
        }
        // 提交成功后才删缓存；若事务回滚，旧库存仍然正确，不需要让读请求提前回填脏数据。
        List<Long> changedProductIds = lines.stream().map(OrderItem::getProductId).toList();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() { changedProductIds.forEach(cache::evict); }
        });
        log.info("Created order id={} items={} total={}", order.getId(), lines.size(), total);
        // 这里只发布进程内事件；RabbitMQ Producer 在事务真正提交后才会收到。
        events.publishEvent(new OrderCreatedEvent(order.getId()));
        return detail(order.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> list() {
        Authentication user = currentUser();
        return isAdmin(user) ? orders.selectAll() : orders.selectAllByOwner(user.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailVO detail(Long id) {
        Order order = orders.selectById(id);
        if (order == null) throw new BusinessException(HttpStatus.NOT_FOUND, "订单不存在");
        Authentication user = currentUser();
        if (!isAdmin(user) && !user.getName().equals(order.getOwnerUsername())) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "不能查看别人的订单");
        }
        return new OrderDetailVO(order, items.selectDetailByOrderId(id));
    }

    private Authentication currentUser() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if (user == null || !user.isAuthenticated()) throw new BusinessException(HttpStatus.UNAUTHORIZED, "请先登录");
        return user;
    }

    private boolean isAdmin(Authentication user) {
        return user.getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()));
    }
}
