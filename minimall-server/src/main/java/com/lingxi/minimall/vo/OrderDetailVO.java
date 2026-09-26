package com.lingxi.minimall.vo;

import com.lingxi.minimall.entity.Order;
import com.lingxi.minimall.entity.OrderItem;
import java.util.List;

/** 给页面的订单详情，把订单头和多条明细组合为一次响应。 */
public record OrderDetailVO(Order order, List<OrderItem> items) {}
