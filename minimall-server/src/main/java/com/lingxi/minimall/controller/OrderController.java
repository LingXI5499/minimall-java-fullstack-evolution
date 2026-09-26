package com.lingxi.minimall.controller;

import com.lingxi.minimall.common.Result;
import com.lingxi.minimall.dto.OrderCreateDTO;
import com.lingxi.minimall.entity.Order;
import com.lingxi.minimall.service.OrderService;
import com.lingxi.minimall.vo.OrderDetailVO;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 订单 HTTP 入口；创建订单的多表事务由 Service 负责。 */
@RestController
@Tag(name = "订单", description = "USER 只能查看自己的订单，ADMIN 可查看全部订单")
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }
    @PostMapping
    @Operation(summary = "创建自己的订单", description = "USER 或 ADMIN；事务中写订单、明细并原子扣减库存")
    public ResponseEntity<Result<OrderDetailVO>> create(@Valid @RequestBody OrderCreateDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Result.created(service.create(body)));
    }
    @GetMapping
    @Operation(summary = "列出可见订单")
    public Result<List<Order>> list() { return Result.success(service.list()); }
    @GetMapping("/{id}")
    @Operation(summary = "查看订单详情")
    public Result<OrderDetailVO> detail(@PathVariable Long id) { return Result.success(service.detail(id)); }
}
