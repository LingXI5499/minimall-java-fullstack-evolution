package com.lingxi.minimall.mapper;

import com.lingxi.minimall.entity.Order;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/** 订单头 SQL 入口，由 MyBatis 代理调用同名 XML 中的语句。 */
@Mapper
public interface OrderMapper {
    int insert(Order order);
    Order selectById(Long id);
    List<Order> selectAll();
}
