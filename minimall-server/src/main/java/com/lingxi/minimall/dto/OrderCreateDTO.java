package com.lingxi.minimall.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

/**
 * 创建订单的请求体。simulateFailure 默认关闭，普通页面只需要提交 items；
 * 教学回滚实验才显式传 true，且服务端还要开启本地演示开关。
 */
public class OrderCreateDTO {
    @NotEmpty(message = "订单明细不能为空")
    private List<@Valid OrderLineDTO> items;
    private boolean simulateFailure;

    public List<OrderLineDTO> items() { return items; }
    public void setItems(List<OrderLineDTO> items) { this.items = items; }
    public boolean simulateFailure() { return simulateFailure; }
    public void setSimulateFailure(boolean simulateFailure) { this.simulateFailure = simulateFailure; }
}
