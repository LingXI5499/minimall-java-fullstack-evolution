package com.lingxi.minimall.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** 一条下单明细：商品 ID 和购买数量，价格始终以后端数据库为准。 */
public record OrderLineDTO(
        @NotNull(message = "商品 ID 不能为空") Long productId,
        @NotNull(message = "数量不能为空") @Min(value = 1, message = "数量至少为 1") Integer quantity) {}
