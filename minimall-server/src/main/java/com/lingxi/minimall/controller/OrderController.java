package com.lingxi.minimall.controller;

import com.lingxi.minimall.common.Result;
import com.lingxi.minimall.dto.OrderCreateDTO;
import com.lingxi.minimall.entity.Order;
import com.lingxi.minimall.service.OrderService;
import com.lingxi.minimall.vo.OrderDetailVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 订单 HTTP 入口；创建订单的多表事务由 Service 负责。 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<Result<OrderDetailVO>> create(@Valid @RequestBody OrderCreateDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Result.created(service.create(body)));
    }
    @GetMapping
    public Result<List<Order>> list() { return Result.success(service.list()); }
    @GetMapping("/{id}")
    public Result<OrderDetailVO> detail(@PathVariable Long id) { return Result.success(service.detail(id)); }
}
