package com.lingxi.minimall.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 唯一索引加 INSERT IGNORE，让重复投递不产生重复通知记录。 */
@Mapper
public interface OrderNotificationMapper {
    int insertIfAbsent(@Param("orderId") Long orderId, @Param("message") String message);
}
