package com.lingxi.minimall.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

/** 创建订单时提交的商品明细；simulateFailure 只用于本地回滚教学。 */
public record OrderCreateDTO(
        @NotEmpty(message = "订单明细不能为空") List<@Valid OrderLineDTO> items,
        boolean simulateFailure) {}
