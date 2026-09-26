package com.lingxi.minimall.mapper;

import com.lingxi.minimall.entity.OrderItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/** 订单明细 SQL 入口；写入与订单头写入处于同一个 Service 事务。 */
@Mapper
public interface OrderItemMapper {
    int insert(OrderItem item);
    List<OrderItem> selectByOrderId(Long orderId);
}
