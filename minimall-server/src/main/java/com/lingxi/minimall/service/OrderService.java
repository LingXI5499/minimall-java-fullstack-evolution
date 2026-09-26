package com.lingxi.minimall.service;

import com.lingxi.minimall.dto.OrderCreateDTO;
import com.lingxi.minimall.entity.Order;
import com.lingxi.minimall.vo.OrderDetailVO;
import java.util.List;

/** 订单业务入口；一个创建动作需要协调商品、订单头和订单明细。 */
public interface OrderService {
    OrderDetailVO create(OrderCreateDTO request);
    List<Order> list();
    OrderDetailVO detail(Long id);
}
